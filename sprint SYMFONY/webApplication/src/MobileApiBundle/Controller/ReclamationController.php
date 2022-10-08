<?php

namespace MobileApiBundle\Controller;
use ReclamationBundle\Entity\Reclamation;
use AppBundle\Entity\User;
use ReclamationBundle\ReclamationBundle;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse as JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Encoder\JsonEncode;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\HttpFoundation\Response;





/**
 * Class ReclamationController
 * @package MobileApiBundle\Controller
 * @Route("/reclamation")
 */
class ReclamationController extends Controller
{


    /**
     * @Route("/getAllrec")
     * @return JsonResponse
     */
    public function boulsAction()
    {
        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository("ReclamationBundle:Reclamation")->findAll();

        return $this->json($reclamation);

    }
    /**
     * @Route("/delete/{idRec}")
     * @return JsonResponse
     */
    public function deleteAction($idRec){
        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository(Reclamation::class)->find($idRec);
        $em->remove($reclamation);
        $em->flush();
        return $this->json(["ok"],200);
    }

    /**
     * @Route("/addRec")
     * @param Request $request
     * @return JsonResponse
     * @throws \Exception
     */
    public function addAction(Request $request){

     //$idrec = $request->get("idRec");
     $iduser = $request->get("idUser");
     $description = $request->get("description");
    // $etat = $request->get("etat");
    // $dateRec = $request->get("dateRec");
    // $reponse = $request->get("reponse");
     $typerec = $request->get("typeRec");
     $user=$this->getDoctrine()->getRepository(User::class)->find($iduser);
     $reclamation =new Reclamation();
        $reclamation->setUser($user);
        $reclamation->setDescription($description);
           $reclamation->setEtat(0);
           $reclamation->setDaterec(new \DateTime('now'));
        //  $reclamation->setReponse($reponse);
           $reclamation->setTyperec($typerec);
        $em = $this->getDoctrine()->getManager();
        $em->persist($reclamation);

        $em->flush();
        return $this->json($reclamation);
    }
}
