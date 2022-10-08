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
 * @Route("/projet")
 */
class ProjetController extends Controller
{
    /**
     * @\Symfony\Component\Routing\Annotation\Route("/getAll")
     */
    public function displayAction()
    {
        $em = $this->getDoctrine()->getManager();

        $projets = $em->getRepository("ProjetBundle:Projet")->findAll();

            return $this->json($projets);
    }

    public function test(Projet $projet)
    {

    }

    /**
     * @Route("/add")
     * @param Request $request
     * @return JsonResponse
     * @throws \Exception
     */
    public function addAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $idprojet = $request->get("idprojet");
        $nom = $request->get("nom");
        $datedebut = $request->get("datedebut");
        $datefin = $request->get("datefin");
        $description = $request->get("description");
        $status = $request->get("status");



        if($idprojet!=null)
            $projet = $this->getDoctrine()->getRepository(Projet::class)->findOneBy(['idprojet'=>intval($idprojet)]);
        else{
            $projet = new Projet();

        }



        $projet
            ->setNom($nom)
            ->setDescription(empty($description)?null:$description)
            ->setDatedebut((new \DateTime())->setTimestamp(intval($datedebut/1000)))
            ->setDatefin((new \DateTime())->setTimestamp(intval($datefin/1000)))
            ->setStatus($status)
        ;

            $em->persist($projet);

            $em->flush();


        return $this->json($projet);

    }

    /**
     * @Route("/delete/{id}")
     * @param Projet $projet
     * @return JsonResponse
     */
    public function deleteAction(Projet $projet)
    {
        $em = $this->getDoctrine()->getManager();
        if(!$projet)
            die("projet n'existe pas");

        $projets = $em->getRepository(Projet::class)->findAll();

        $em->remove($projet);
        $em->flush();
        return $this->json(["ok"],200);
    }


}
