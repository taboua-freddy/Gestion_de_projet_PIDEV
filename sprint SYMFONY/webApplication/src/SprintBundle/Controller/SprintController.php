<?php
/**
 * Created by PhpStorm.
 * User: arafe
 * Date: 13/04/2020
 * Time: 22:16
 */


namespace SprintBundle\Controller;


use Doctrine\Common\Collections\ArrayCollection;
use SprintBundle\Entity\Sprint;
use SprintBundle\Entity\UserStory;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use TacheBundle\Entity\Tache;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;

/**
 * User controller.
 *
 * @Route("dashboard/sprints")
 */
class SprintController extends Controller
{
    /**
     * @Route("/", name="sprints_index")
     * @Method({"GET", "POST"})
     */
    public function indexSprintAction( Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $sprints = $em->getRepository('SprintBundle:Sprint')->findAll();

        return $this->render('@Sprint/Sprint/index.html.twig', array(
            'sprints' => $sprints,
        ));
    }


    /**
     * @Route("/edit/{id}", name="sprint_edit")
     * @Method({"GET", "POST"})
     */
    public function editSprintAction( Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $sprint = $em->getRepository('SprintBundle:Sprint')->find($id);

        $editForm = $this->createForm('SprintBundle\Form\SprintType', $sprint);

        if ($editForm->isSubmitted() && $editForm->isValid()) {
            $this->getDoctrine()->getManager()->flush();
            return $this->redirectToRoute('sprints_index');
        }

        return $this->render('@Sprint/Sprint/edit.html.twig', array(
            'edit_form' => $editForm->createView(),
        ));
    }

    /**
     * @Route("/new", name="sprint_new")
     * @Method({"GET", "POST"})
     */
    public function addSprintAction( Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $sprint = new Sprint();

        $form = $this->createForm('SprintBundle\Form\SprintType', $sprint);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($sprint);
            $em->flush();
            return $this->redirectToRoute('sprints_index');
        }

        return $this->render('@Sprint/Sprint/new.html.twig', array(
            'form' => $form->createView(),
        ));
    }

    /**
     * @Route("/delete/{id}", name="sprint_delete")
     * @Method({"GET", "POST"})
     */
    public function deletSprintAction( Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $sprints = $em->getRepository('SprintBundle:Sprint')->find($id);

        $em->remove($sprints);
        $em->flush();
        return $this->redirectToRoute('sprints_index');

    }


    /**
     * @Route("/show/{id}", name="sprint_show")
     * @Method({"GET", "POST"})
     */
    public  function  showAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $sprint = $em->getRepository('SprintBundle:Sprint')->find($id);
        $userstories = $em->getRepository('SprintBundle:UserStory')->findBy(['sprint'=> $sprint ]);

        return $this->render('@Sprint/Sprint/show.html.twig', array(
            'sprint' => $sprint,
            'userstories' => $userstories,
        ));
    }

    /**
     * @Route("/annulerUserStory/{id}", name="sprint_show_annuler_us")
     * @Method({"GET", "POST"})
     */
    public  function  annulerUserStoryAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $us = $em->getRepository('SprintBundle:UserStory')->find($id);
        $spr = $us->getSprint();
        $us->setSprint(null);
        $em->flush();
        return $this->redirectToRoute('sprint_show', ['id' => $spr->getIdsprint()]);

    }
}