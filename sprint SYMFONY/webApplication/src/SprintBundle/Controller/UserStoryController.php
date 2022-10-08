<?php
/**
 * Created by PhpStorm.
 * User: arafe
 * Date: 09/04/2020
 * Time: 11:50
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
 * @Route("dashboard/userstories")
 */
class UserStoryController extends Controller
{


    /**
     * @Route("/addUserStory/{id}", name="user_story_new")
     * @Method({"GET", "POST"})
     */
    public function addUserStoriesAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();

        $userStory = new UserStory();
        $userStoryForm = $this->createForm('SprintBundle\Form\UserStoryType', $userStory);
//        dump($userStoryForm);die;


        if ($userStoryForm->isSubmitted() && $userStoryForm->isValid()) {
            $em->persist($userStory);
            $em->flush();

            return $this->redirectToRoute('user_stories_index', array('id' => $userStory->getProjet()->getIdprojet()));
        }

        return $this->render('@Sprint/UserStories/new.html.twig', array(
            'form' => $userStoryForm->createView(),
            'id' => $id
        ));
    }


    /**
     * @Route("/{id}", name="user_stories_index")
     * @Method({"GET", "POST"})
     */
    public function listUserStoriesAction($id, Request $request)
    {

//        if (!$this->get('security.authorization_checker')->isGranted('ROLE_ADMIN')) {
//            throw new AccessDeniedException("Vous n'êtes pas autorisés à accéder à cette page!", Response::HTTP_FORBIDDEN);
//        }

        $em = $this->getDoctrine()->getManager();
        $project = $em->getRepository('ProjetBundle:Projet')->find($id);
        $userStories = $em->getRepository('SprintBundle:UserStory')->findBy(['projet' => $project]);


        $sprint = new Sprint();
        $sprintForm = $this->createForm('SprintBundle\Form\SprintType', $sprint);

        return $this->render('@Sprint/UserStories/index.html.twig', array(
            'sprintForm' => $sprintForm->createView(),
            'userStories' => $userStories,
            'project' => $project,
        ));

    }



    /**
     * @Route("/delete/{id}", name="user_story_delete")
     * @Method({"GET", "POST"})
     */
    public function deleteUserStoriesAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $us = $em->getRepository('SprintBundle:UserStory')->find(['idstory' => $id]);
        $projet = $us->getProjet();

        $em->remove($us);
        $em->flush();
        return $this->redirectToRoute('user_stories_index', array('id' => $projet->getIdprojet()));
    }


    /**
     * @Route("/addSprint/{id}", name="add_new_sprint")
     * @Method({"GET", "POST"})
     */
    public function addSprintAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $projet = $em->getRepository('ProjetBundle:Projet')->find($id);


        $allUs = new ArrayCollection();
        $allUsId = $request->get('allUs');
        foreach ($allUsId as $idUs) {
            $userStory = $em->getRepository('SprintBundle:UserStory')->find($idUs);
            $allUs->add($userStory);
        }

        $dateDeb = new \DateTime($request->get('dateDeb'));
        $dateFin = new \DateTime($request->get('dateFin'));
        $nom = $request->get('nom');

        $sprint = new Sprint();

        $sprint->setDatedebut($dateDeb);
        $sprint->setDatefin($dateFin);
        $sprint->setNomsprint($nom);
        $sprint->setProjet($projet);
        $sprint->setUserstories($allUs);

        $em->persist($sprint);
        $em->flush();

        foreach ($allUs as $uss) {
            $uss->setSprint($sprint);
            $em->flush();
        }

        return $this->redirectToRoute('user_stories_index', array('id' => $projet->getIdprojet()));
    }




    /**
     * Finds and displays a user entity.
     *
     * @Route("/edit/{id}", name="user_story_edit")
     * @Method({"GET", "POST"})
     */
    public function editAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $userStory = $this->getDoctrine()->getManager()->getRepository('SprintBundle:UserStory')->find($id);
        $user = $this->get('security.token_storage')->getToken()->getUser();

        $editForm = $this->createForm('SprintBundle\Form\UserStoryType', $userStory);
        $editForm->handleRequest($request);
        $projet = $userStory->getProjet();
        if ($editForm->isSubmitted() && $editForm->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('user_stories_index', array('id' => $projet->getIdprojet()));
        }

        return $this->render('@Sprint/UserStories/edit.html.twig', array(
            'projet' => $projet,
            'userStory' => $userStory,
            'edit_form' => $editForm->createView(),
        ));
    }

    /**
     * @Route("/show/{id}", name="user_story_show")
     * @Method({"GET", "POST"})
     */
    public  function  showAction(Request $request, $id)
    {
        $em = $this->getDoctrine()->getManager();
        $userStory = $this->getDoctrine()->getManager()->getRepository('SprintBundle:UserStory')->find($id);
        $taches = $em->getRepository('TacheBundle:Tache')->findBy(['userstory'=> $userStory ]);

        return $this->render('@Sprint/UserStories/show.html.twig', array(
            'us' => $userStory,
            'taches' => $taches,
        ));
    }



}