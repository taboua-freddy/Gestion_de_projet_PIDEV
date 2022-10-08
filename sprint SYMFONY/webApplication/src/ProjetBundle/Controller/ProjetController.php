<?php

namespace ProjetBundle\Controller;

use ProjetBundle\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\FormInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use CMEN\GoogleChartsBundle\GoogleCharts\Charts\PieChart;
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\HttpFoundation\Response;

/**
 * Projet controller.
 *
 */
class ProjetController extends Controller
{
    /**
     * Lists all projet entities.
     * @param Request $request
     * @return Response
     */
    public function indexAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();

        $projets = $em->getRepository('ProjetBundle:Projet')->findAll();

        if ($request->isMethod('post')) {
            $q = $request->get('search');
            $projets = $em->getRepository('ProjetBundle:Projet')->Search($q);
        }
        return $this->render('projet/index.html.twig', array(
            'projets' => $projets,
        ));
    }
    public function deleAction($idprojet)
    {
        $em=$this->getDoctrine()->getManager();
        $grp=$em->getRepository('ProjetBundle:Projet')->find($idprojet+0);

        $em->remove($grp);
        $em->flush();
       return $this->redirectToRoute('projet_index');


    }
    public function aymenAction()
    {

        return $this->render('projet/indexUser.html.twig');
    }
    public function indexPdfAction()
    {
        $em = $this->getDoctrine()->getManager();

        $projets = $em->getRepository('ProjetBundle:Projet')->findAll();


        return $this->render('projet/indexPdf.html.twig', array(
            'projets' => $projets,
        ));
    }

    public function PDFAction()
    {
        $em = $this->getDoctrine()->getManager();
        $projets = $em->getRepository('ProjetBundle:Projet')->findAll();
        $dompdf = new Dompdf();
        $html = $this->renderView('projet/indexPdf.html.twig', array(
            'projets' => $projets,

        ));
        $dompdf->loadHtml($html);
        $dompdf->set_option('defaultFont', 'Courier');
        $dompdf->setPaper('A4', 'landscape');

        $dompdf->render();

        $dompdf->stream();
        return $this->redirectToRoute('projet_index');

    }

    public function changeStatAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $projets = $em->getRepository('ProjetBundle:Projet')->find($request->get('spece') + 0);

        $projets->setStatus($request->get('selc'));
        $em->persist($projets);
        $em->flush();

        return $this->redirectToRoute('projet_index');
    }

    /**
     * Creates a new projet entity.
     * @param Request $request
     * @return RedirectResponse|Response
     */
    public function newAction(Request $request)
    {
        $projet = new Projet();
        $form = $this->createForm('ProjetBundle\Form\ProjetType', $projet);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {

            $now = new \DateTime('now');


            $Mydate = $projet->getDatedebut();
            $MydateFin = $projet->getDatefin();
            $diff = date_diff($Mydate, $now)->format('%a') + 0;

            if ($diff >= 1 && $Mydate > $now) {
                $projet->setStatus('NOTSTARTED');

            } else
                $projet->setStatus('INPROGRESS');

            $em = $this->getDoctrine()->getManager();
            $em->persist($projet);
            $em->flush();

            return $this->redirectToRoute('projet_index');
        }

        return $this->render('projet/new.html.twig', array(
            'projet' => $projet,
            'form' => $form->createView(),
        ));
    }

    /**
     * Finds and displays a projet entity.
     * @param Projet $projet
     * @return Response
     */
    public function showAction(Projet $projet)
    {
        $deleteForm = $this->createDeleteForm($projet);

        return $this->render('projet/show.html.twig', array(
            'projet' => $projet,
            'delete_form' => $deleteForm->createView(),
        ));
    }

    /**
     * Displays a form to edit an existing projet entity.
     * @param Request $request
     * @param Projet $projet
     * @return RedirectResponse|Response
     */
    public function editAction(Request $request, Projet $projet)
    {
        $deleteForm = $this->createDeleteForm($projet);
        $editForm = $this->createForm('ProjetBundle\Form\ProjetType', $projet);
        $editForm->handleRequest($request);

        if ($editForm->isSubmitted() && $editForm->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('projet_edit', array('idprojet' => $projet->getIdprojet()));
        }

        return $this->render('projet/edit.html.twig', array(
            'projet' => $projet,
            'edit_form' => $editForm->createView(),
            'delete_form' => $deleteForm->createView(),
        ));
    }

    /**
     * Deletes a projet entity.
     * @param Request $request
     * @param Projet $projet
     * @return RedirectResponse
     */
    public function deleteAction(Request $request, Projet $projet)
    {
        $form = $this->createDeleteForm($projet);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->getDoctrine()->getManager();
            $em->remove($projet);
            $em->flush();
        }

        return $this->redirectToRoute('projet_index');
    }

    /**
     * Creates a form to delete a projet entity.
     *
     * @param Projet $projet The projet entity
     *
     * @return FormInterface
     */
    private function createDeleteForm(Projet $projet)
    {
        return $this->createFormBuilder()
            ->setAction($this->generateUrl('projet_delete', array('idprojet' => $projet->getIdprojet())))
            ->setMethod('DELETE')
            ->getForm();
    }

    public function chartPieAction(Request $request)
    {
        $pieChart = new PieChart();
        $em = $this->getDoctrine();
        $hold = $em->getRepository(Projet::class)->findBy(array('status' => 'OnHold'));
        $progrss = $em->getRepository(Projet::class)->findBy(array('status' => 'Inprogress'));
        $finished = $em->getRepository(Projet::class)->findBy(array('status' => 'Finished'));
        $nt = $em->getRepository(Projet::class)->findBy(array('status' => 'Notstarted'));

        $pieChart->getData()->setArrayToDataTable([
            ['Status', 'Number of Projects', 'Total'],
            ['OnHold', count($hold), count($hold) + count($progrss) + count($finished) + count($nt)],
            ['Inprogress', count($progrss), count($hold) + count($progrss) + count($finished) + count($nt)],
            ['Finished', count($finished), count($hold) + count($progrss) + count($finished) + count($nt)],
            ['Notstarted', count($nt), count($hold) + count($progrss) + count($finished) + count($nt)],

        ]);
        $pieChart->getOptions()->setTitle('Projects stat ');
        $pieChart->getOptions()->setHeight(500);
        $pieChart->getOptions()->setWidth(900);
        $pieChart->getOptions()->getTitleTextStyle()->setBold(true);
        $pieChart->getOptions()->getTitleTextStyle()->setColor('#009900');
        $pieChart->getOptions()->getTitleTextStyle()->setItalic(true);
        $pieChart->getOptions()->getTitleTextStyle()->setFontName('Arial');
        $pieChart->getOptions()->getTitleTextStyle()->setFontSize(20);
        return $this->render('@Projet\Default\stat.html.twig', array('piechart' => $pieChart));

    }
}
