<?php
/**
 * Created by PhpStorm.
 * User: arafe
 * Date: 10/04/2020
 * Time: 23:12
 */

namespace SprintBundle\Controller;

use Doctrine\Common\Collections\ArrayCollection;
use SprintBundle\Entity\UserStory;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use TacheBundle\Entity\Tache;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;

/**
 * User controller.
 *
 * @Route("dashboard/taches")
 */
class TacheController extends Controller
{

    /**
     * @Route("/", name="taches_index")
     * @Method({"GET", "POST"})
     */
    public function listTachesAction( Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $user = $this->get('security.token_storage')->getToken()->getUser();

        $userStory = new UserStory();
        $userStoryForm = $this->createForm('SprintBundle\Form\UserStoryType', $userStory);



        $taches = $em->getRepository('TacheBundle:Tache')->findAll();

        return $this->render('@Sprint/Taches/index.html.twig', array(
            'taches' => $taches,
            'userStoryForm' => $userStoryForm->createView(),
        ));
    }

    /**
     * @Route("/add", name="taches_new")
     * @Method({"GET", "POST"})
     */
    public function newAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();

        $tache = new Tache();
        $tacheForm = $this->createForm('SprintBundle\Form\TacheType', $tache);
        $tacheForm->handleRequest($request);

        if ($tacheForm->isSubmitted() && $tacheForm->isValid()) {
            $em->persist($tache);
            $em->flush();

            return $this->redirectToRoute('taches_index');
        }
        return $this->render('@Sprint/Taches/new.html.twig', array(
            'form' => $tacheForm->createView(),
        ));
    }


    /**
     * @Route("/delete/{id}", name="tache_delete")
     * @Method({"GET", "POST"})
     */
    public function deleteTacheAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $t = $em->getRepository('TacheBundle:Tache')->find(['idtache' => $id]);

        $em->remove($t);
        $em->flush();
        return $this->redirectToRoute('taches_index');
    }



    /**
     * @Route("/edit/{id}", name="tache_edit")
     * @Method({"GET", "POST"})
     */
    public function editTacheAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $t = $em->getRepository('TacheBundle:Tache')->find(['idtache' => $id]);
        $tacheForm = $this->createForm('SprintBundle\Form\TacheType', $t);
        $tacheForm->handleRequest($request);

        if ($tacheForm->isSubmitted() && $tacheForm->isValid()) {
            $em->flush();

            return $this->redirectToRoute('taches_index');
        }

        return $this->render('@Sprint/Taches/new.html.twig', array(
            'form' => $tacheForm->createView(),
        ));
    }

}