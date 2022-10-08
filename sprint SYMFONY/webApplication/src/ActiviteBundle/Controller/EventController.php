<?php

namespace ActiviteBundle\Controller;

use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Form\EvenementType;
use ActiviteBundle\Service\FacebookApi;
use ActiviteBundle\Service\TemplatesData;
use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Exception;
use ProjetBundle\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class EventController extends Controller
{
    /**
     * @Route("/add_event" ,name="add_event")
     */
    public function addAction()
    {
        $utils = new Utils();
        $event = new Evenement();
        $form = $this->createForm(EvenementType::class,$event,[
            "action"=>$this->generateUrl("save_event"),
        ]);

        return $this->render("@Activite/add-event.html.twig",[
            "form"=>$form->createView(),
            "uniteTemps"=>$utils->getUniteTemps(),
        ]);
    }

    /**
     * @Route("/save_modify_event/{id}", name="save_modify_event")
     * @Route("/save_event",name="save_event")
     * @param Request $request
     * @param Evenement $event
     * @return JsonResponse
     * @throws Exception
     */
    public function saveAction(Request $request,Evenement $event = null)
    {
        $utils = new Utils();
        $success = [];
        $errors = [];

        if($event==null)
            $event = new Evenement();

        $form = $this->createForm(EvenementType::class,$event);
        $form->handleRequest($request);

        $meRappeler = $request->get("meRappeler");
        $dureeRappel = $request->get("dureeRappel");
        $uniteTemps = $request->get("uniteTemps");
        $projets = $request->get("data-pure");

        if($form->isSubmitted()&&$form->isValid())
        {

            if($meRappeler=="on")
            {
                $dateDebut = new \DateTime();
                $dateDebut->setTimestamp($event->getDatedebut()->getTimestamp());
                $rappel = $dateDebut->sub(new \DateInterval("PT" . $dureeRappel * $uniteTemps . "M"));
                $event->setDaterappel($rappel);
            }
            else
                $event->setDaterappel(null);

            $em = $this->getDoctrine()->getManager();

            $userNotification = $em->getRepository(User::class)->findAll();
            $event->setUsersNotification($userNotification);

            if($event->getIdevent()!=null)
            {
                $event->removeProjets();
            }

            if($projets!=null)
            {
                foreach ($projets as $projet_id)
                {
                    $projet = $em->getRepository(Projet::class)->find($projet_id);
                    $event->addProjet($projet);
                }
            }
            $em->persist($event);

            $em->flush();
            $success = ["Evénement programmer"];
        }

        $errors = $utils->getFormErrorsTree($form);

        return $this->json(["success"=>$success, "errors"=>$errors]);
    }
    /**
     * @Route("/modify_event/{id}")
     * @return RedirectResponse|Response
     */
    public function modifyeventAction(Evenement $event = null)
    {
        if(!$event)
            return $this->redirectToRoute("display_meeting");

        $utils = new Utils();
        $form = $this->createForm(EvenementType::class,$event,[
            'action' =>$this->generateUrl('save_modify_event',["id"=>$event->getIdevent()])
        ]);
        return $this->render("@Activite/modify-event.html.twig",[
            "form"=>$form->createView(),
            "uniteTemps"=>$utils->getUniteTemps(),
        ]);

    }
    /**
     * get all Event details
     * @Route("/get_event_Detail/{id}" ,name="get_event_Detail")
     * @param Evenement $event
     * @return false|string
     * @throws \Exception
     */
    public function ajaxGetEventDetails(Evenement $event=null)
    {
        if(!$event)
            die("aucun event ne correspond");

        $temp = new TemplatesData();
        $temp->setBasePath( $this->get('assets.packages'));

        return $this->json([
            "details"=>$temp->templateDetailsEvent($event),
        ]);
    }

    /**
     * @Route("/get_projets/{id_event}" ,name="get_projets")
     * @param $id_event
     * @return JsonResponse
     */
    public function getListsProjetsAction($id_event)
    {
        $utils = new Utils();
        $em = $this->getDoctrine();
        $projets = $em->getRepository(Projet::class)->findAll();
        $response = [
            "projets"=>[],
            "projetsSelect"=>[]
        ];
        /**
         * @TODO - a revoir
         */
        if($id_event!=0)
        {
            /**
             * @var Evenement $event
             */
            $event = $em->getRepository(Evenement::class)->find($id_event);
            $projetss = $event->getProjets();
            foreach ($projetss as $projet)
                array_push($response["projetsSelect"],strval($projet->getIdprojet()));
        }
        foreach ($projets as $projet) {
            $temp = [
                "label"=>($projet->getNom()),
                "value"=>"{$projet->getIdprojet()}"
            ];
            array_push($response["projets"],$temp);
        }
        return $this->json($response);
    }

}
