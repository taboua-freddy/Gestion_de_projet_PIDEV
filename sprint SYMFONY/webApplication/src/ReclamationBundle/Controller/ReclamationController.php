<?php


namespace ReclamationBundle\Controller;


use ActiviteBundle\Service\Utils;
use Doctrine\DBAL\Types\TextType;
use ReclamationBundle\Entity\Rate;
use ReclamationBundle\Entity\Reclamation;
use ReclamationBundle\Form\ReclamationRep;
use ReclamationBundle\Repository\ReclamationsRepository;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use ReclamationBundle\Form\ReclamationType;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use CMEN\GoogleChartsBundle\GoogleCharts\Options\PieChart;


class ReclamationController extends Controller
{
    /**
     * @Route("/addRec" ,name="addRec")
     * @param Request $request
     * @return RedirectResponse|Response
     */
    public function addRecAction(Request $request)
    {
        $rec = new Reclamation();
        $form = $this->createForm(ReclamationType::class, $rec);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $rec->setDaterec(new \DateTime('now'));
            dump($this->getUser());
            $rec->setUser($this->getUser());
            $rec->setEtat(0);
            $em = $this->getDoctrine()->getManager();
            $em->persist($rec);
            $em->flush();
            return $this->redirect('/');
        }

        return $this->render('@Reclamation/reclamation_ajout.html.twig', [
            'form' => $form->createView()
        ]);
    }

    /**
     * @Route("afficherRec",name="afficherRec")
     */
    public function afficherRecAction()
    {
        $test=$this->getUser()->getUsername();
        $em=$this->getDoctrine()->getManager();
        if($test==="admin")
        {
            $reclamation=$this->getDoctrine()->getRepository(Reclamation::class)->findAll();
            return  $this->redirectToRoute('rechercheRec');
            ;
        }
        else {
            //$reclamation = $this->getDoctrine()->getRepository(Reclamation::class)->findBy([
            //  'user' => $this->getUser()
            $reclamation= $em->getRepository(Rate::class)->getTab();
            // ]);
        }
        $i = 0;
        foreach ($reclamation as $reclam) {
            $rate = $em->getRepository(Rate::class)->getRateId($reclam["idRec"]);
            $raate = $em->getRepository(Rate::class)->getRateIdd($reclam["idRec"]);
            $reclamation[$i]["like"] = $rate[0][1];
            dump($raate);
            $reclamation[$i]["dislike"] = $raate[0][1];

            $i++;
        }
        return $this->render('@Reclamation/afficherRec.html.twig', [
            'recs' => $reclamation
        ]);
    }

    /**
     * @Route("supprimerRec/{idrec}",name="supprimerRec")
     */
    public function supprimerRecAction($idrec)
    {
        $utlis = new Utils();
        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository(Reclamation::class)->find($idrec);
        $em->remove($reclamation);
        $em->flush();
        if (!$utlis->isSimpleUser($this->getUser()->getRoles())) {
            return $this->redirectToRoute('rechercheRec');
        } else
            return $this->redirectToRoute('afficherRec');

    }

    /**
     * @Route("/repondRec/{idrec}" ,name="repondRec")
     * @param Request $request
     * @return RedirectResponse|Response
     */
    public function repondreRecAction(Request $request, $idrec)
    {
        $reclamation = new Reclamation();
        $reclamation = $this->getDoctrine()->getRepository(Reclamation::class)->find($idrec);
        $form = $this->createForm(ReclamationRep::class, $reclamation);
        /*$form = $this->createFormBuilder($reclamation)->add('reponse',TextType::class,array('attr'))->add
        ('repondre',SubmitType::class,array('label'=>'repondre','attr'))->getForm();
        */
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $reclamation->setDaterec(new \DateTime('now'));

            $reclamation->setEtat(1);
            $em = $this->getDoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('rechercheRec');
        }
        return $this->render('@Reclamation/reply.html.twig', [
            'form' => $form->createView()
        ]);

    }

    /**
     * @Route("/rechercheRec" ,name="rechercheRec")
     */

    public function rechercheByDateAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository(Rate::class)->getTab();

        if ($request->isMethod("POST")) {

            $date = $request->get('date');

            // $reclamation =$em->getRepository("ReclamationBundle:Reclamation")->findBy(array('daterec'=>(new \DateTime("$date"))));
            $reclamation = $em->getRepository(Rate::class)->getDate($date);


        }
        $i = 0;
        foreach ($reclamation as $reclam) {
            $rate = $em->getRepository(Rate::class)->getRateId($reclam["idRec"]);
            $raate = $em->getRepository(Rate::class)->getRateIdd($reclam["idRec"]);
            $reclamation[$i]["like"] = $rate[0][1];
            dump($raate);
            $reclamation[$i]["dislike"] = $raate[0][1];

            $i++;
        }
        return $this->render('@Reclamation/recherche.html.twig', [
            'recs' => $reclamation
        ]);
    }

    /**
     * @Route("/satatRec" ,name="satatRec")
     */
    public function statisRecAction()
    {
        $pieChart = new \CMEN\GoogleChartsBundle\GoogleCharts\Charts\PieChart();

        $em = $this->getDoctrine();
        $reclamation = $this->getDoctrine()->getRepository(Reclamation::class)->findAll();
        $var1 = "meeting";
        $i = 0;
        $j = 0;

        foreach ($reclamation as $reclamations) {
            if (strcmp($reclamations->getTyperec(), $var1) == 0) {
                $i = $i + 1;
            } else
                $j = $j + 1;

        }
        $pieChart->getData()->setArrayToDataTable(
            [['reclamation', 'typeRec'],
                ['event', $j],
                ['meeting', $i],

            ]
        );
        $pieChart->getOptions()->setTitle('pourcentage des reclamations selon le type ');
        $pieChart->getOptions()->setHeight(500);
        $pieChart->getOptions()->setWidth(900);
        $pieChart->getOptions()->getTitleTextStyle()->setBold(true);
        $pieChart->getOptions()->getTitleTextStyle()->setColor('#009900');
        $pieChart->getOptions()->getTitleTextStyle()->setItalic(true);
        $pieChart->getOptions()->getTitleTextStyle()->setFontName('Arial');
        $pieChart->getOptions()->getTitleTextStyle()->setFontSize(20);

        return $this->render('@Reclamation/statRec.html.twig.', array('pieChart' => $pieChart)
        );


    }

    /**
     * @Route("/likeRec/{idrec}" ,name="likeRec")
     */

    public function LikeAction($idrec)
    {

        $test = $this->getUser()->getId();

        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository(Reclamation::class)->find($idrec);
        $rate = $em->getRepository(Rate::class)->findAll();
        foreach ($rate as $rates) {

            if ($rates->getUser()->getId() == $test) {
                if ($rates->getReclamation()->getIdrec() == $idrec) {

                    $raty = $em->getRepository(Rate::class)->findBy(['Reclamation' => $reclamation]);
                    // $raty = $em->getRepository(Rate::class)->findBy(array('val'=>1));
                    foreach ($raty as $raat) {
                        if ($raat->getUser()->getId() == $test) {
                            $em->remove($raat);
                        }
                    }
                    $rat = new Rate();
                    $rat->setUser($this->getUser());
                    $rat->setReclamation($reclamation);
                    $rat->setval(1);
                    $em->persist($rat);
                    $em->flush();
                    return $this->redirectToRoute('rechercheRec');
                }
            }

        }

        $rat = new Rate();
        $rat->setUser($this->getUser());
        $rat->setReclamation($reclamation);
        $rat->setval(1);
        $em->persist($rat);
        $em->flush();

        $rate = $em->getRepository(Rate::class)->getRateId(1);


        return $this->redirectToRoute('rechercheRec');


    }

    /**
     * @Route("/dislikeRec/{idrec}" ,name="dislikeRec")
     */

    public function DislikeAction($idrec)
    {

        $test = $this->getUser()->getId();

        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository(Reclamation::class)->find($idrec);
        $rate = $em->getRepository(Rate::class)->findAll();
        foreach ($rate as $rates) {

            if ($rates->getUser()->getId() == $test) {
                if ($rates->getReclamation()->getIdrec() == $idrec) {

                    $raty = $em->getRepository(Rate::class)->findBy(['Reclamation' => $reclamation]);
                    // $raty = $em->getRepository(Rate::class)->findBy(array('val'=>1));
                    foreach ($raty as $raat) {
                        if ($raat->getUser()->getId() == $test) {
                            $em->remove($raat);
                        }
                    }
                    $rat = new Rate();
                    $rat->setUser($this->getUser());
                    $rat->setReclamation($reclamation);
                    $rat->setval(0);
                    $em->persist($rat);
                    $em->flush();
                    return $this->redirectToRoute('rechercheRec');
                }
            }

        }

        $rat = new Rate();
        $rat->setUser($this->getUser());
        $rat->setReclamation($reclamation);
        $rat->setval(0);
        $em->persist($rat);
        $em->flush();

        $rate = $em->getRepository(Rate::class)->getRateId(1);


        return $this->redirectToRoute('rechercheRec');

    }
}