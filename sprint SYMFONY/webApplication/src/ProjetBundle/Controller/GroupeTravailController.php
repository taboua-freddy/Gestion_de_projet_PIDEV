<?php

namespace ProjetBundle\Controller;

use AppBundle\Entity\User;
use ProjetBundle\Entity\GroupeTravail;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\FormInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Swift_Attachment;
use Swift_Mailer;
use Swift_SmtpTransport;
use Symfony\Component\HttpFoundation\Response;

/**
 * Groupetravail controller.
 *
 */
class GroupeTravailController extends Controller
{
    /**
     * Lists all groupeTravail entities.
     * @param Request $request
     * @return Response
     */
    public function indexAction(Request $request)
    {

        $em = $this->getDoctrine()->getManager();

        $groupeTravails = $em->getRepository('ProjetBundle:GroupeTravail')->findAll();
        if($request->isMethod('post')){
            $q=$request->get('search');
            $groupeTravails = $em->getRepository('ProjetBundle:GroupeTravail')->Search($q);
        }
        return $this->render('groupetravail/index.html.twig', array(
            'groupeTravails' => $groupeTravails,
        ));
    }

    /**
     * Creates a new groupeTravail entity.
     * @param Request $request
     * @return RedirectResponse|Response
     */
    public function newAction(Request $request)
    {
        $groupeTravail = new Groupetravail();
        $form = $this->createForm('ProjetBundle\Form\GroupeTravailType', $groupeTravail);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->getDoctrine()->getManager();

            $em->persist($groupeTravail);
            $em->flush();

            return $this->redirectToRoute('groupetravail_show', array('idgroupe' => $groupeTravail->getIdgroupe()));
        }

        return $this->render('groupetravail/new.html.twig', array(
            'groupeTravail' => $groupeTravail,
            'form' => $form->createView(),
        ));
    }

    /**
     * Finds and displays a groupeTravail entity.
     * @param GroupeTravail $groupeTravail
     * @return Response
     */
    public function showAction(GroupeTravail $groupeTravail)
    {$em = $this->getDoctrine()->getManager();
       // $deleteForm = $this->createDeleteForm($groupeTravail);

        $usrs = $em->getRepository('ProjetBundle:GroupeTravail')->findByRole("ROLE_USER");

        return $this->render('groupetravail/show.html.twig', array(
            'groupeTravail' => $groupeTravail,
        //    'delete_form' => $deleteForm->createView(),
            'users'=>$usrs,
        ));
    }
    public function affectAction(Request $request,$idprojet)
    {
        $transporter = Swift_SmtpTransport::newInstance('smtp.gmail.com', 465, 'ssl')
            ->setUsername('velov638@gmail.com')
            ->setPassword('Velo123456789.');

        $mailer = Swift_Mailer::newInstance($transporter);
        $em=$this->getDoctrine()->getManager();



        if($request->isMethod('post')){
            $idd=$request->get('data')+0;

            $classe=$em->getRepository('ProjetBundle:GroupeTravail')->find($idprojet+0);

            $user=$em->getRepository('AppBundle:User')->find($idd+0);

            $sujet="you are assigned successfully to the Group  :".$classe->getNom();

          $message = (new \Swift_Message('Hello Email'))
                ->setFrom('velov638@gmail.com')
                ->setTo($user->getEmail())
                ->setBody($sujet)
            ;

            $mailer->send($message);

            $classe->addUser($user);


            $em->persist($classe);
            $em->flush();
        }
        return $this->redirectToRoute('groupetravail_show', array('idgroupe' => $classe->getIdgroupe()));
    }

    /**
     * Displays a form to edit an existing groupeTravail entity.
     * @param Request $request
     * @param GroupeTravail $groupeTravail
     * @return RedirectResponse|Response
     */
    public function editAction(Request $request, GroupeTravail $groupeTravail)
    {
        $deleteForm = $this->createDeleteForm($groupeTravail);
        $editForm = $this->createForm('ProjetBundle\Form\GroupeTravailType', $groupeTravail);
        $editForm->handleRequest($request);

        if ($editForm->isSubmitted() && $editForm->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('groupetravail_index');
        }

        return $this->render('groupetravail/edit.html.twig', array(
            'groupeTravail' => $groupeTravail,
            'edit_form' => $editForm->createView(),
            'delete_form' => $deleteForm->createView(),
        ));
    }
    public function removeAction($idgroupe)
    {
      $em=$this->getDoctrine()->getManager();
      $grp=$em->getRepository('ProjetBundle:GroupeTravail')->find($idgroupe+0);

        $em->remove($grp);
        $em->flush();
            return $this->redirectToRoute('groupetravail_index');


    }

    /**
     * Deletes a groupeTravail entity.
     * @param Request $request
     * @param GroupeTravail $groupeTravail
     * @return RedirectResponse
     */
    public function deleteAction(Request $request, GroupeTravail $groupeTravail)
    {
        $form = $this->createDeleteForm($groupeTravail);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->getDoctrine()->getManager();
            $em->remove($groupeTravail);
            $em->flush();
        }

        return $this->redirectToRoute('groupetravail_index');
    }

    /**
     * Creates a form to delete a groupeTravail entity.
     *
     * @param GroupeTravail $groupeTravail The groupeTravail entity
     *
     * @return FormInterface
     */
    private function createDeleteForm(GroupeTravail $groupeTravail)
    {
        return $this->createFormBuilder()
            ->setAction($this->generateUrl('groupetravail_delete', array('idgroupe' => $groupeTravail->getIdgroupe())))
            ->setMethod('DELETE')
            ->getForm()
        ;
    }
}
