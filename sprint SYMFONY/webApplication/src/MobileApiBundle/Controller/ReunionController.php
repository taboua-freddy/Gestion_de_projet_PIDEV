<?php

namespace MobileApiBundle\Controller;

use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Entity\Objectif;
use ActiviteBundle\Entity\PresenceReunion;
use ActiviteBundle\Entity\Reunion;
use ActiviteBundle\Service\GoogleCalendar;
use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use ProjetBundle\Entity\GroupeTravail;
use ProjetBundle\Entity\Projet;
use SprintBundle\Entity\Sprint;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse as JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * Class ReunionController
 * @Route("/meeting")
 * @package MobileApiBundle\Controller
 */
class ReunionController extends Controller
{

    /**
     * get sprint by id projet
     * @Route("/change_presence/{reunion}/{user}" )
     * @param Reunion $reunion
     * @param User $user
     * @return JsonResponse
     */
    public function ajaxMarqueAbsence(Reunion $reunion,User $user)
    {
        if(!$reunion || !$user)
            die("reunion n'existe pas");
        $response = [];
        $em = $this->getDoctrine()->getManager();
        /**
         * @var PresenceReunion $presenceReunion
         */
        $presenceReunion = $em->getRepository(PresenceReunion::class)->findOneBy(["user"=>$user,"reunion"=>$reunion]);
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
     * @Route("/add_google_calendar/{user}/{type}/{id}")
     * @param User $user
     * @param Request $request
     * @param $type
     * @param $id
     * @param $token
     * @return Response
     * @throws \Exception
     */
    public function googleCalendarAction(User $user, Request $request,$type,$id)
    {
        $success = [];
        $errors = [];
        $session = $request->getSession();
        $token = $request->get("token");
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
     * @route("/link_token")
     */
    public function displayAction()
    {
        $google = new GoogleCalendar();
        $authUrl = $google->getAuthUrl();
        return $this->json($authUrl);
    }
    /**
     * @Route("/add/{admin}")
     * @param User $admin
     * @param Request $request
     * @return JsonResponse
     * @throws \Exception
     */
    public function addAction(User $admin,Request $request)
    {
        if(!$admin)
            die("l'utlisateur n'existe pas");

        $id = $request->get("idReunion");
        $titre = $request->get("titre");
        $dateDebut = $request->get("dateDebut");
        $dateFin = $request->get("dateFin");
        $description = $request->get("description");
        $objectifs = $request->get("objectifs");
        $sprint = $request->get("sprint");
        $coordo = $request->get("coordonateur");
        $theme = $request->get("theme");
        $importance = $request->get("importance");
        $participants = $request->get("participants");
        $meRappeler = $request->get("meRappeler");
        $dureeRappel = $request->get("dureeRappel");
        $uniteTemps = $request->get("uniteTemps");

        $em = $this->getDoctrine()->getManager();
        if($id!=null && intval($id)>0)
            $reunion = $em->getRepository(Reunion::class)->findOneBy(['idreunion'=>intval($id)]);
        else
            $reunion = new Reunion();

        $sprint = (intval($sprint)==0)?null:$em->getRepository(Sprint::class)->findOneBy(["idsprint"=>intval($sprint)]);

        $coordo = (intval($coordo)==0)?null:$em->getRepository(User::class)->findOneBy(["id"=>intval($coordo)]);

        $participants = empty($participants)?[]:explode(" ",$participants);

        $reunion
            ->setTitre($titre)
            ->setDescription(empty($description)?null:$description)
            ->setDatedebut((new \DateTime())->setTimestamp(intval($dateDebut/1000)))
            ->setDatefin((new \DateTime())->setTimestamp(intval($dateFin/1000)))
            ->setSprint($sprint)
            ->setCoordonateur($coordo)
            ->setThemedujour(empty($theme)?null:$theme)
            ->setImportance($importance)
        ;

        if(!empty($reunion->getTitre()))
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
                $em->persist(new Objectif($objectifs,$reunion));
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
            array_push($userNotification,$admin);
            $reunion->setUsersNotification($userNotification);
            $em->persist($reunion);
            $em->flush();
        }
        return $this->json($reunion);
    }

    /**
     * @Route("/delete/{reunion}/{user}")
     * @param Reunion $reunion
     * @param User $user
     * @return JsonResponse
     */
    public function deleteAction(Reunion $reunion,User $user)
    {
        if(!$reunion || !$user)
            die("reunion n'existe pas");

        $em = $this->getDoctrine()->getManager();
        $reunion = $this->setUserNotification($reunion,$user);

        $em->remove($reunion);
        $em->flush();
        return $this->json(["ok"],200);
    }

    /**
     * @Route("/get_allMeetings/{idUser}")
     * @return JsonResponse
     */
    public function getMeetingsAction($idUser)
    {
        $em = $this->getDoctrine()->getManager();
        $user =  $em->getRepository(User::class)->findOneBy(["id"=>$idUser]);
        if($user==null)
            die("l'utilisateur n'existe pas");
        $utils = new Utils();

        if($utils->isSimpleUser($user->getRoles()) )
        {
            $userId = $user->getId();
            $reunions = $em->getRepository("ActiviteBundle:Reunion")->findByUserId($userId);
        }
        else
            $reunions = $em->getRepository("ActiviteBundle:Reunion")->findAll();

        return $this->json($reunions);
    }

    /**
     * @Route("/get_objectifs/{idReunion}")
     * @param $idReunion
     * @return JsonResponse
     */
    public function getObjectifsAction($idReunion)
    {
        $objectifs = [];
        $em = $this->getDoctrine()->getManager();
        $objectif = $em->getRepository(Objectif::class)->findBy(["reunion"=>$idReunion]);
        //$reunion = array_merge($reunion,$objectifs);
        foreach ($objectif as $ob)
        {
            array_push($objectifs,[
                "objectif"=>$ob->getObjectif(),
            ]);
        }
        return $this->json($objectifs);
    }

    /**
     * @Route("/get_projets")
     * @return JsonResponse
     */
    public function getProjetsAction()
    {
        $projets = $this->getDoctrine()->getRepository(Projet::class)->findAll();
        return $this->json($projets);
    }

    /**
     * @Route("/get_participants/{idReunion}")
     * @param $idReunion
     * @return JsonResponse
     */
    public function getParticipantsAction(Reunion $idReunion)
    {
        $data = [];
        /** @var User[] $users */
        $users = $this->getDoctrine()->getRepository(Reunion::class)->getParticipantsSelected($idReunion);

        foreach ($users as $user)
        {
            /** @var User $userG */
            $user = $this->getDoctrine()->getRepository(User::class)->findOneBy(["id"=>$user->getId()]) ;
            /** @var PresenceReunion $presence */
            $presence = $this->getDoctrine()->getRepository(PresenceReunion::class)->findOneBy(["reunion"=>$idReunion,"user"=>$user]);

            array_push($data,[
                "iduser"=>$user->getId(),
                "nom"=>$user->getNom(),
                "prenom"=>$user->getPrenom(),
                "groupe"=>$this->getNomGroupe($user),
                "presence"=>$presence->getPresence(),
            ]);
        }
        return $this->json($data);
    }

    /**
     * @Route("/get_sprints/{idProjet}")
     * @param $idProjet
     * @return JsonResponse
     */
    public function getSprintsAction($idProjet)
    {
        $sprints = $this->getDoctrine()->getRepository(Reunion::class)->getSprintProjet($idProjet);
        return $this->json($sprints);
    }

    /**
     * @Route("/get_users")
     * @return JsonResponse
     */
    public function getUsersAction()
    {

        /** @var User[] $users */
        $users = $this->getDoctrine()->getRepository(User::class)->findAll();
        $data = [];

        foreach ($users as $user)
        {
            array_push($data,[
               "iduser"=>$user->getId(),
                "nom"=>$user->getNom(),
                "prenom"=>$user->getPrenom(),
                "groupe"=>$this->getNomGroupe($user),
            ]);
        }

        return $this->json($data);
    }

    /**
     * @param Reunion $reunion
     * @param User $admin
     * @return Reunion
     */
    public function setUserNotification(Reunion $reunion,User $admin)
    {
        $userNotification = [];
        /** @var PresenceReunion[] $presencesReunion */
        $presencesReunion = $reunion->getPresenceReunion();
        foreach ($presencesReunion as $presence )
        {
            array_push($userNotification,$presence->getUser());
        }
        array_push($userNotification,$admin);
        $reunion->setUsersNotification($userNotification);

        return $reunion;
    }

    public function getNomGroupe(User $user)
    {
        $groupes = $user->getGroupe();
        $nomGroupe = "";

        if($groupes->count()==0)
            $nomGroupe = "Sans groupe";
        else
        {
            foreach ($groupes as $groupe)
                $nomGroupe .= $groupe->getNom() . "-";
        }

        return $nomGroupe;
    }
}
