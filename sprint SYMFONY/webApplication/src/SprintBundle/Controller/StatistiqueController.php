<?php
/**
 * Created by PhpStorm.
 * User: arafe
 * Date: 14/04/2020
 * Time: 15:51
 */

namespace SprintBundle\Controller;


use Symfony\Bundle\FrameworkBundle\Controller\Controller;

use CMEN\GoogleChartsBundle\GoogleCharts\Charts\Histogram;
use CMEN\GoogleChartsBundle\GoogleCharts\Charts\PieChart;
use CMEN\GoogleChartsBundle\GoogleCharts\Charts\ScatterChart;
use Symfony\Component\HttpFoundation\Request;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;


/**
 * User controller.
 *
 * @Route("statistiques")
 */
class StatistiqueController extends Controller
{
    /**
     * Nombre de user stories par projet
     * @Route("/projets", name="projet_statitistique")
     * @Method("GET")
     */
    public function statProjetAction(Request $request)
    {
        if (!$this->get('security.authorization_checker')->isGranted('ROLE_ADMIN')) {
            return $this->redirectToRoute('homepage');
        }

        $sprintPerProjet = $this->sprintPerProjet();
        $usPerProjet = $this->usPerProjet();
        $etatUsPerProjet = $this->etatUsPerProjet();

        return $this->render('@Sprint/Statistique/usParProjet.html.twig', array(
            'sprintPerProjet' => $sprintPerProjet,
            'usPerProjet' => $usPerProjet,
            'etatUsPerProjet' => $etatUsPerProjet,
        ));

    }

    /**
     * Nombre de user stories par projet
     * @Route("/sprints", name="sprint_statitistique")
     * @Method("GET")
     */
    public function statSprintAction(Request $request)
    {
        if (!$this->get('security.authorization_checker')->isGranted('ROLE_ADMIN')) {
            return $this->redirectToRoute('homepage');
        }

        $etatUsPerProjet = $this->etatUsPerSprint();

        return $this->render('@Sprint/Statistique/sprint.html.twig', array(
            'etatUsPerProjet' => $etatUsPerProjet,
        ));

    }

    public function sprintPerProjet()
    {
        $pieChart = new PieChart();
        $em = $this->getDoctrine()->getManager();
        $allSprints = $em->getRepository('SprintBundle:Sprint')->findAll();
        $allProjet = $em->getRepository('ProjetBundle:Projet')->findAll();

        $data = array();

        array_push($data, ['Libelle', 'Nombre de user story']);
        foreach ($allProjet as $projet) {
            foreach ($allSprints as $sp) {
                $nbr = $em->getRepository('SprintBundle:Sprint')->findBy(['projet' => $projet]);
            }
            array_push($data, [$projet->getNom(), count($nbr)]);
        }

        $pieChart->getData()->setArrayToDataTable($data);
        $pieChart->getOptions()->setTitle('Nombre de sprint par projet');
        $pieChart->getOptions()->setHeight(400);
        $pieChart->getOptions()->setWidth(400);
        $pieChart->getOptions()->getTitleTextStyle()->setBold(true);
        $pieChart->getOptions()->getTitleTextStyle()->setColor('#009900');
        $pieChart->getOptions()->getTitleTextStyle()->setItalic(true);
        $pieChart->getOptions()->getTitleTextStyle()->setFontName('Arial');
        $pieChart->getOptions()->getTitleTextStyle()->setFontSize(20);
        return $pieChart;
    }
    public function usPerProjet()
    {
        $pieChart = new PieChart();
        $em = $this->getDoctrine()->getManager();
        $allProjet = $em->getRepository('ProjetBundle:Projet')->findAll();
        $allUserStories = $em->getRepository('SprintBundle:UserStory')->findAll();

        $data = array();
        $nbr = [];
        array_push($data, ['Libelle', 'Nombre de user story']);
        foreach ($allProjet as $projet) {
            foreach ($allUserStories as $sp) {
                $nbr = $em->getRepository('SprintBundle:UserStory')->findBy(['projet' => $projet]);
            }
            array_push($data, [$projet->getNom(), count($nbr)]);
        }
        $pieChart->getData()->setArrayToDataTable($data);
        $pieChart->getOptions()->setTitle('Nombre user story par projet');
        $pieChart->getOptions()->setHeight(400);
        $pieChart->getOptions()->setWidth(400);
        $pieChart->getOptions()->getTitleTextStyle()->setBold(true);
        $pieChart->getOptions()->getTitleTextStyle()->setColor('#009900');
        $pieChart->getOptions()->getTitleTextStyle()->setItalic(true);
        $pieChart->getOptions()->getTitleTextStyle()->setFontName('Arial');
        $pieChart->getOptions()->getTitleTextStyle()->setFontSize(20);
        return $pieChart;
    }
        public function etatUsPerProjet()
    {
        $chart = new \CMEN\GoogleChartsBundle\GoogleCharts\Charts\Material\ColumnChart();
        $em = $this->getDoctrine()->getManager();
        $allProjet = $em->getRepository('ProjetBundle:Projet')->findAll();
        $allUserStories = $em->getRepository('SprintBundle:UserStory')->findAll();


        $data = array();
        $nbrToDo =[];
        $nbrDoing=[];
        $nbrDone=[];
        array_push($data, ['Projet', 'TO DO', 'DOING', 'DONE']);
        foreach ($allProjet as $projet) {
            foreach ($allUserStories as $sp) {
                $nbrToDo = $em->getRepository('SprintBundle:UserStory')->findBy(['projet' => $projet, 'status' => 'TODO']);
                $nbrDoing = $em->getRepository('SprintBundle:UserStory')->findBy(['projet' => $projet, 'status' => 'DOING']);
                $nbrDone = $em->getRepository('SprintBundle:UserStory')->findBy(['projet' => $projet, 'status' => 'DONE']);
            }
            array_push($data, [$projet->getNom(), count($nbrToDo), count($nbrDoing), count($nbrDone)]);
        }
        $chart->getData()->setArrayToDataTable($data);
        $chart->getOptions()->getChart()
            ->setTitle('Avancement global des projets')
            ->setSubtitle('Todo, Doing, and Done');
        $chart->getOptions()
            ->setBars('vertical')
            ->setHeight(400)
            ->setWidth(900)
            ->setColors(['#1b9e77', '#d95f02', '#7570b3'])
            ->getVAxis()
            ->setFormat('decimal');

        return $chart;
    }


    public function etatUsPerSprint()
    {
        $chart = new \CMEN\GoogleChartsBundle\GoogleCharts\Charts\Material\ColumnChart();
        $em = $this->getDoctrine()->getManager();
        $allSprint = $em->getRepository('SprintBundle:Sprint')->findAll();
        $allUserStories = $em->getRepository('SprintBundle:UserStory')->findAll();


        $data = array();
        $nbrToDo =[];
        $nbrDoing=[];
        $nbrDone=[];
        array_push($data, ['Projet', 'TO DO', 'DOING', 'DONE']);
        foreach ($allSprint as $sprint) {
            foreach ($allUserStories as $sp) {
                $nbrToDo = $em->getRepository('SprintBundle:UserStory')->findBy(['sprint' => $sprint, 'status' => 'TODO']);
                $nbrDoing = $em->getRepository('SprintBundle:UserStory')->findBy(['sprint' => $sprint, 'status' => 'DOING']);
                $nbrDone = $em->getRepository('SprintBundle:UserStory')->findBy(['sprint' => $sprint, 'status' => 'DONE']);
            }
            array_push($data, ['#'.$sprint->getIdsprint() . ' ' .$sprint->getNomsprint(), count($nbrToDo), count($nbrDoing), count($nbrDone)]);
        }
        $chart->getData()->setArrayToDataTable($data);
        $chart->getOptions()->getChart()
            ->setTitle('Avancement global des sprints')
            ->setSubtitle('Todo, Doing, and Done');
        $chart->getOptions()
            ->setBars('vertical')
            ->setHeight(400)
            ->setWidth(900)
            ->setColors(['#1b9e77', '#d95f02', '#7570b3'])
            ->getVAxis()
            ->setFormat('decimal');

        return $chart;
    }

}