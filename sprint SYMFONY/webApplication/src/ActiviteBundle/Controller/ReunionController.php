<?php

namespace ActiviteBundle\Controller;

use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Entity\Objectif;
use ActiviteBundle\Entity\PresenceReunion;
use ActiviteBundle\Entity\Reunion;
use ActiviteBundle\Form\ReunionType;
use ActiviteBundle\Service\GoogleCalendar;
use ActiviteBundle\Service\TemplatesData;
use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Doctrine\Common\Collections\ArrayCollection;
use ProjetBundle\Entity\GroupeTravail;
use ProjetBundle\Entity\Projet;
use Swift_Mailer;
use Swift_Message;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ReunionController extends Controller
{
    /**
     * @Route("/add_meeting" ,name="add_meeting")
     */
    public function addAction()
    {
        
        $userss = $this->getDoctrine()->getRepository(User::class)->findAll();
        $projets = $this->getDoctrine()->getRepository(Projet::class)->findAll();
        $utils = new Utils();
        $reunion = new Reunion();
        $form = $this->createForm(ReunionType::class,$reunion,[
            'action' =>$this->generateUrl('save_meeting')
        ]);

        return $this->render('@Activite/add-meeting.html.twig',[
            "form"=>$form->createView(),
            "typeImpotance"=>$utils->getTypeImpotance(),
            "typeSprint"=>$utils->getTypeSprint(),
            "groupes"=>$utils->groupeUser($userss),
            "projets"=>$projets,
            "uniteTemps"=>$utils->getUniteTemps(),
        ]);
    }

    /**
     * get sprint by id projet
     * @Route("/get_sprint/{id}" ,name="get_sprint")
     */
    public function ajaxGetSprint($id)
    {
        $response = [];
        $sprints = $this->getDoctrine()->getRepository(Reunion::class)->getSprintProjet($id);
        for ($i=0;$i<count($sprints);$i++)
        {
            $sprint = $sprints[$i];
            $response[$i]=[
                "id"=>$sprint->getIdsprint(),
                "nom"=>$sprint->getNomsprint(),
                "date_debut"=>$sprint->getDatedebut()->format("D-M-yy"),
            ];
        }
        return $this->json($response);
    }

    /**
     * get sprint by id projet
     * @Route("/marquer_presence/{id}" ,name="marquer_presence")
     * @param Reunion $reunion
     * @return JsonResponse
     * @throws \Exception
     */
    public function ajaxMarqueAbsence(Reunion $reunion)
    {
        if(!$reunion)
            die("reunion n'existe pas");
        $response = [];
        $em = $this->getDoctrine()->getManager();
        /**
         * @var PresenceReunion $presenceReunion
         */
        $presenceReunion = $em->getRepository(PresenceReunion::class)->findOneBy(["user"=>$this->getUser(),"reunion"=>$reunion]);
        $presence = $presenceReunion->getPresence();
        if($reunion->getDatedebut() > new \DateTime() )
        {
            if($presence == 0 )
            {
                $presenceReunion->setPresence(1);
                $response['success'] = ["presence"=>["vous seriez present à la reunion ".$reunion->getTitre()]];
            }
            else
            {
                $presenceReunion->setPresence(0);
                $response['success'] = ["presence"=>["vous seriez absent à la reunion ".$reunion->getTitre()]];
            }
            $em->persist($presenceReunion);
            $em->flush();
        }
        else
            $response['errors'] = ["presence"=>["La reunion ".$reunion->getTitre()." est déjà passée"]];

        $response['presence'] = $presenceReunion->getPresence();

        return $this->json($response);
    }


    /**
     * @Route("/get_participants/{id_reunion}" ,name="get_participants")
     */
    public function getListsParticipantsAction($id_reunion)
    {
        $utils = new Utils();
        $userss = $this->getDoctrine()->getRepository(User::class)->findAll();
        $response = [
            "participants"=>[],
            "participantsSelect"=>[]
        ];
        if($id_reunion!=0)
        {
            $users = $this->getDoctrine()->getRepository(Reunion::class)->getParticipantsSelected($id_reunion);
            foreach ($users as $user)
                array_push($response["participantsSelect"],strval($user->getId()));
        }
        $groupes = $utils->groupeUser($userss);
        foreach ($groupes as $groupe=>$users) {
            foreach ($users as $user) {
                $temp = [
                    "label"=>($user->getNom()." ".$user->getPrenom()." /".$groupe),
                    "value"=>"{$user->getId()}"
                ];
                array_push($response["participants"],$temp);
            }
        }
        return $this->json($response);
    }

    /**
     * @Route("/save_modify_meeting/{id}", name="save_modify_meeting")
     * @Route("/save_meeting",name="save_meeting")
     * @param Swift_Mailer $mailer
     * @param Request $request
     * @param Reunion|null $reunion
     * @return JsonResponse
     * @throws \Exception
     */
    public function saveAction(Request $request,Reunion $reunion=null,Swift_Mailer $mailer=null)
    {

        $utils = new Utils();
        $success = [];
        $errors = [];

        if($reunion==null)
            $reunion = new Reunion();

        $form = $this->createForm(ReunionType::class,$reunion);
        $form->handleRequest($request);

        $meRappeler = $request->get("meRappeler");
        $dureeRappel = $request->get("dureeRappel");
        $uniteTemps = $request->get("uniteTemps");
        $objectifs = $request->get("objectifs");
        $participants = $request->get("data-pure");


        if($form->isSubmitted()&&$form->isValid())
        {
            if($meRappeler=="on")
            {
                $dateDebut = new \DateTime();
                $dateDebut->setTimestamp($reunion->getDatedebut()->getTimestamp());
                $rappel = $dateDebut->sub(new \DateInterval("PT" . $dureeRappel * $uniteTemps . "M"));
                $reunion->setDaterappel($rappel);
            }
            else
                $reunion->setDaterappel(null);

            $em = $this->getDoctrine()->getManager();

            if($reunion->getIdreunion()!=null)
            {
                $presencesR = $em->getRepository(PresenceReunion::class)->findBy(["reunion"=>$reunion]);
                foreach ($presencesR as $presenceR)
                {
                    $em->remove($presenceR);
                }
                $reunion->setPresenceReunion(new ArrayCollection());

                $objs = $em->getRepository("ActiviteBundle:Objectif")->findBy(["reunion"=>$reunion]);
                foreach ($objs as $objectif)
                {
                    $em->remove($objectif);
                }
            }

            $em->persist($reunion);

            if($objectifs!=null)
            {
                foreach ($objectifs as $objectif)
                {
                    $em->persist(new Objectif($objectif,$reunion));
                }
            }
            $userNotification=[];
            if($participants!=null)
            {
                //$this->sendMail($participants,$mailer,"Meeting",$reunion->getIdreunion());
                foreach ($participants as $participant)
                {
                    /** @var User $user */
                    $user = $em->getRepository(User::class)->find($participant);
                    if($user!=null)
                    {
                        array_push($userNotification,$user);
                        $em->persist(new PresenceReunion($user,$reunion,1));
                    }
                }
            }
            array_push($userNotification,$this->getUser());
            $reunion->setUsersNotification($userNotification);
            $em->persist($reunion);
            $em->flush();
            $success = ["Réunion programmer"];
        }

        $errors = $utils->getFormErrorsTree($form);
        return $this->json(["success"=>$success, "errors"=>$errors]);
    }

    /**
     * @route("/display_meeting",name="display_meeting")
     */
    public function displayAction()
    {
        $google = new GoogleCalendar();
        $authUrl = $google->getAuthUrl();
        return $this->render('@Activite/display-meeting.html.twig',[
            'authUrl'=>$authUrl,
        ]);
    }

    /**
     * get all mettings for calandar API
     * @Route("/get_meetings" ,name="get_meetings")
     * @throws \Exception
     */
    public function ajaxGetMeetings(Request $request)
    {
        $utils = new Utils();
        $em = $this->getDoctrine();
        $user = $this->getUser();

        if($this->getUser() && $utils->isSimpleUser($user->getRoles()) )
        {
            $userId = $user->getId();
            $reunions = $em->getRepository("ActiviteBundle:Reunion")->findByUserId($userId);
        }
        else
            $reunions = $em->getRepository("ActiviteBundle:Reunion")->findAll();


        $events = $em->getRepository("ActiviteBundle:Evenement")->findAll();

        // Short-circuit if the client did not give us a date range.
        if (!($request->get('start')) || !($request->get('end'))) {
            die("Please provide a date range.");
        }
        $start = $request->get('start'); $end = $request->get('end');
        $result = [];
        foreach ($reunions as $reunion)
        {
            array_push($result,[
                "id"=>$reunion->getIdreunion(),
                "title"=>$reunion->getTitre(),
                "start"=>$reunion->getDatedebut()->format("yy-m-dTH:m:s"),
                "end"=>$reunion->getDatefin()->format("yy-m-dTH:m:s"),
                "color"=>$utils->getColorCalendar()[$reunion->getImportance()],
                "groupId"=>"meeting"
            ]);
        }
        foreach ($events as $event)
        {
            array_push($result,[
                "id"=>$event->getIdevent(),
                "title"=>$event->getTitre(),
                "start"=>$event->getDatedebut()->format("yy-m-dTH:m:s"),
                "end"=>$event->getDatefin()->format("yy-m-dTH:m:s"),
                "groupId"=>"event"
            ]);
        }
        $output_arrays = $utils->getReunion($start,$end,$request->get('time_zone'),$result);
        return $this->json($output_arrays);
    }

    /**
     * get all metting details
     * @Route("/get_meeting_Detail/{id}" ,name="get_meeting_Detail")
     * @param Reunion $reunion
     * @return false|string
     * @throws \Exception
     */
    public function ajaxGetMeetingDetails(Reunion $reunion=null)
    {
        if(!$reunion)
            die("aucune reunion ne correspond");

        $em = $this->getDoctrine();
        $presence = -1;
        /**@var PresenceReunion[] $pRs*/
        $pRs =  $reunion->getPresenceReunion();
        foreach ($pRs as $pR)
        {
            if($pR->getUser()===$this->getUser() && $pR->getReunion() === $reunion)
                $presence = $pR->getPresence();
        }

        $objectifs = $em->getRepository('ActiviteBundle:Objectif')->findBy(["reunion"=>$reunion]);
        $temp = new TemplatesData();

        return $this->json([
            "details"=>$temp->templateDetailsMeeting($reunion,$objectifs),
            "participants"=>$temp->templateParticipantsMeeting($reunion),
            "presence"=>$presence,
        ]);
    }

    /**
     * @Route("/delete_activity/{type}/{id}")
     * @param $id
     * @param $type
     * @return JsonResponse
     */
    public function ajaxDeleteActivity($id, $type)
    {
        $em = $this->getDoctrine()->getManager();
        if($type=="meeting")
        {
            $reunion = $em->getRepository('ActiviteBundle:Reunion')->find(intval($id));
            if(!$reunion)
                die("reunion n'existe pas");

            $reunion = $this->setUserNotification($reunion);

            $em->remove($reunion);
            $em->flush();

            $message= "la réunion ". $reunion->getTitre() . " a été supprimée";
        }
        else
        {
            $event = $em->getRepository(Evenement::class)->find(intval($id));
            if(!$event)
                die("evenement n'existe pas");

            $userNotification = $em->getRepository(User::class)->findAll();
            $event->setUsersNotification($userNotification);

            $em->remove($event);
            $em->flush();
            $message= "l' événement ". $event->getTitre() . " a été supprimé";
        }

        return $this->json([
            "success"=>[$message],
        ]);
    }

    /**
     * @Route("/get_objectifs/{id}")
     * @param $id
     * @return JsonResponse
     */
    public function ajaxGetObjectifs($id)
    {
        $em = $this->getDoctrine()->getManager();
        $reunion = $em->getRepository("ActiviteBundle:Reunion")->find($id);
        $objectifs = $em->getRepository("ActiviteBundle:Objectif")->findBy(["reunion"=>$reunion]);

        if(!$objectifs)
            return $this->json([]);
        $data =[];
        foreach ($objectifs as $objectif)
            array_push($data,$objectif->getObjectif());

        return $this->json($data);
    }

    /**
     * @Route("/modify_meeting/{id}")
     * @return RedirectResponse|Response
     */
    public function modifyMeetingAction($id)
    {
        $em = $this->getDoctrine()->getManager();
        $reunion = $em->getRepository("ActiviteBundle:Reunion")->find($id);
        if(!$reunion)
            return $this->redirectToRoute("display_meeting");

        $sprint = $em->getRepository("SprintBundle:Sprint")->find($reunion->getSprint());
        $userss = $em->getRepository(User::class)->findAll();
        $projets = $em->getRepository(Projet::class)->findAll();
        $utils = new Utils();
        $form = $this->createForm(ReunionType::class,$reunion,[
            'action' =>$this->generateUrl('save_modify_meeting',["id"=>$reunion->getIdreunion()])
        ]);
        return $this->render("@Activite/modify-meeting.html.twig",[
            "form"=>$form->createView(),
            "typeImpotance"=>$utils->getTypeImpotance(),
            "typeSprint"=>$utils->getTypeSprint(),
            "groupes"=>$utils->groupeUser($userss),
            "projets"=>$projets,
            "uniteTemps"=>$utils->getUniteTemps(),
            "sprint"=>$sprint,
        ]);
    }

    /**
     * @Route("/add_Google_Calendar/{type}/{id}" , name="add_Google_Calendar")
     * @param Request $request
     * @param $type
     * @param $id
     * @return Response
     * @throws \Exception
     */
    public function googleCalendarAction(Request $request,$type,$id)
    {
        $success = [];
        $errors = [];
        $session = $request->getSession();
        $token = $request->get("token");
        /**@var User $user **/
        $user = $this->getUser();
        if(!$user)
            die("vous n'etes pas connecté");

        $email = $user->getEmail();
        $google = new GoogleCalendar();
        if($token && !empty($token))
        {
            $session->set("token_calendar",$token);
        }

        if(!$token || empty($token))
        {
            $token = $session->get("token_calendar");
        }

        if ($token && !empty($token))
        {
            $google->insertGoogleToken($token,$email);
            $em = $this->getDoctrine();
            if($type=="meeting")
            {
                $reunion = $em->getRepository(Reunion::class)->findOneBy(["idreunion"=>$id]);
                if(!$reunion)
                    die("aucune reunion ne coorespond");
                $participants =  $em->getRepository('ActiviteBundle:PresenceReunion')->findBy(["reunion"=>$reunion]);
                foreach ($participants as $participant)
                {
                    $user = $em->getRepository('AppBundle:User')->findOneBy(["id"=>$participant->getUser()->getId()]);
                    $participant->setUser($user);
                }
                $reunion->setPresenceReunion($participants);
                $event = $google->setMeeting($reunion);
                $success = [" Reunion ". $reunion->getTitre() ." Ajouté à Google Calendar"];
            }
            else
            {
                $evenement = $em->getRepository(Evenement::class)->find($id);
                if(!$evenement)
                    die("aucun evenement ne coorespond");
                $event = $google->setEvent($evenement);
                $success = [" Evénement ". $evenement->getTitre() ." Ajouté à google Calendar"];
            }
            $google->saveMeeting($event);
        }
        else
        {
            $errors = ["token"=>["votre token est vide"]];
        }

        return $this->json(["success"=>$success,"errors"=>$errors]);
    }

    /**
     * @param Reunion $reunion
     * @return Reunion
     */
    public function setUserNotification(Reunion $reunion)
    {
        $userNotification = [];
        /** @var PresenceReunion[] $presencesReunion */
        $presencesReunion = $reunion->getPresenceReunion();
        foreach ($presencesReunion as $presence )
        {
            array_push($userNotification,$presence->getUser());
        }
        array_push($userNotification,$this->getUser());
        $reunion->setUsersNotification($userNotification);

        return $reunion;
    }

    /**
     * @param User[] $users
     * @param Swift_Mailer $mailer
     * @param string $object
     * @param int $id
     */
    public function sendMail(array $users, Swift_Mailer $mailer, $object,$id)
    {
        $to = [];
        foreach ($users as $user)
        {
            array_push($to,[$user->getEmail() => $user->getNom() ." " .$user->getPrenom()]);
        }
        $text = "hello";
        $message = (new Swift_Message($object))
            ->setFrom('send@example.com')
            ->setTo($to)
            ->addPart(
                $this->renderView(
                // app/Resources/views/Emails/registration.html.twig
                    '@Activite/mail.html.twig',[
                        "id"=>$id,
                        "message"=>$text
                    ]
                ),
                'text/plain'
            )

            // you can remove the following code if you don't define a text version for your emails
           /* ->addPart(
                $this->renderView(
                    'Emails/registration.txt.twig',
                    ['name' => $name]
                ),
                'text/plain'
            )*/
        ;

        $mailer->send($message);

    }

}
