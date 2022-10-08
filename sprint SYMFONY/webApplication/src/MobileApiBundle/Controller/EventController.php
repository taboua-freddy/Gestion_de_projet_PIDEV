<?php

namespace MobileApiBundle\Controller;

use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Form\EvenementType;
use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use ProjetBundle\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\DependencyInjection\Container;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * Class DefaultController
 * @package MobileApiBundle\Controller
 * @Route("/event")
 */
class EventController extends Controller
{
    /**
     * @\Symfony\Component\Routing\Annotation\Route("/getAll")
     */
    public function displayAction()
    {
        $em = $this->getDoctrine()->getManager();
        $events = $em->getRepository("ActiviteBundle:Evenement")->findAll();
        return $this->json($events);
    }

    /**
     * @Route("/add")
     * @param Request $request
     * @return JsonResponse
     * @throws \Exception
     */
    public function addAction(Request $request)
    {
        $id = $request->get("idEvent");
        $titre = $request->get("titre");
        $dateDebut = $request->get("dateDebut");
        $dateFin = $request->get("dateFin");
        $description = $request->get("description");
        $affiche = $request->get("affiche");
        $lieu = $request->get("lieu");

        $meRappeler = $request->get("meRappeler");
        $dureeRappel = $request->get("dureeRappel");
        $uniteTemps = $request->get("uniteTemps");


        if($id!=null)
            $event = $this->getDoctrine()->getRepository(Evenement::class)->findOneBy(['idevent'=>intval($id)]);
        else
            $event = new Evenement();

        if(isset($_FILES['fileUpload']))
        {
            $event
                ->setImageSize($_FILES["fileUpload"]["size"])
                ->setImageName($_FILES["fileUpload"]["name"]);
            $this->uploadImage();
        }
        else
        {
            if($affiche && empty($affiche))
            {
                unlink ($this->get('kernel')->getProjectDir()."\web\uploads\images\\events\\".$event->getImageName());
                $event
                    ->setImageSize(null)
                    ->setImageName(null);
            }

        }

        $event
            ->setTitre($titre)
            ->setDescription(empty($description)?null:$description)
            ->setDatedebut((new \DateTime())->setTimestamp(intval($dateDebut/1000)))
            ->setDatefin((new \DateTime())->setTimestamp(intval($dateFin/1000)))
        ;

        $meRappeler = $request->get("meRappeler");
        $dureeRappel = $request->get("dureeRappel");
        $uniteTemps = $request->get("uniteTemps");
        $projets = $request->get("data-pure");

        if($titre && $dateDebut)
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
        }

        return $this->json($event);

    }

    /**
     * @Route("/delete/{id}")
     * @param Evenement $event
     * @return JsonResponse
     */
    public function deleteAction(Evenement $event)
    {
        $em = $this->getDoctrine()->getManager();
        if(!$event)
            die("evenement n'existe pas");

        $userNotification = $em->getRepository(User::class)->findAll();
        $event->setUsersNotification($userNotification);

        $em->remove($event);
        $em->flush();
        return $this->json(["ok"],200);
    }


    public function uploadImage()
    {
        $allow = array("jpg", "jpeg", "gif", "png");

        $todir = $this->get('kernel')->getProjectDir()."\web\uploads\images\\events\\";

        if ( !!$_FILES['fileUpload']['tmp_name'] ) // is the file uploaded yet?
        {
            $info = explode('.', strtolower( $_FILES['fileUpload']['name']) ); // whats the extension of the file

            if ( in_array( end($info), $allow) ) // is this file allowed
            {
                if ( move_uploaded_file( $_FILES['fileUpload']['tmp_name'], $todir . basename($_FILES['fileUpload']['name'] ) ) )
                {
                    // the file has been moved correctly
                }
            }
            else
            {
                // error this file ext is not allowed
            }
        }
    }
}
