<?php

namespace SprintMobileBundle\Controller;

use DateTime;
use ProjetBundle\Entity\Projet;
use SprintBundle\Entity\Sprint;
use SprintBundle\Entity\UserStory;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\HttpFoundation\Request;

class SprintController extends Controller
{
    public function fetchSprintsAction($id)
    {
        $em = $this->getDoctrine()->getManager();
        $sprints = $em->getRepository(Sprint::class)->findBy(array('projet' => $id));
        /*foreach ($sprints as $pst) {

            $pst->setDatedebut($pst->getDatedebut()->format('Y-m-d'));
            $pst->setDatefin($pst->getDatefin()->format('Y-m-d'));
        }*/

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getIdsprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($sprints,true);
        return new JsonResponse($sprints);
    }
    public function AddSprintAction($name, $dateDebut, $dateFin,$idProjet)
    {
        $em = $this->getDoctrine()->getManager();
        $projet = $em->getRepository(Projet::class)->findOneBy(array('idprojet' => $idProjet));
        $date_debut    =   DateTime::createFromFormat("Y-m-d",$dateDebut);
        $date_fin    =   DateTime::createFromFormat("Y-m-d",$dateFin);

        $sprint = new Sprint();
        $sprint->setNomsprint($name);
        $sprint->setDatedebut($date_debut);
        $sprint->setDatefin($date_fin);
        $sprint->setProjet($projet);

        $em->persist($sprint);
        $em->flush();

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getIdsprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($sprint);
        return new JsonResponse($formatted);
    }

    public function UpdateSprintAction($id,$name, $dateDebut, $dateFin)
    {

        $em = $this->getDoctrine()->getManager();
        $sprint = $em->getRepository(Sprint::class)->find($id);
        $date_debut    =   DateTime::createFromFormat("Y-m-d",$dateDebut);
        $date_fin    =   DateTime::createFromFormat("Y-m-d",$dateFin);
        $sprint->setNomsprint($name);
        $sprint->setDatedebut($date_debut);
        $sprint->setDatefin($date_fin);

        $em->persist($sprint);
        $em->flush();

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getIdsprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($sprint);
        return new JsonResponse($formatted);

    }


    public function deleteSprintAction($id)
    {
        $em = $this->getDoctrine()->getManager();
        $sprint = $em->getRepository(Sprint::class)->find($id);
        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($sprint);

        $em->remove($sprint);
        $em->flush();

        return new JsonResponse($formatted);
    }


    public function fetchStoriesAction($id)
    {
        $em = $this->getDoctrine()->getManager();
        $stories = $em->getRepository(UserStory::class)->findBy(array('sprint' => $id));

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(1);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getSprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($stories,true);
        return new JsonResponse($formatted);
    }

    public function AddStoryAction($name, $bv, $cap,$complexite,$roi,$priorite,$status,$description,$idProjet,$idSprint)
    {
        $em = $this->getDoctrine()->getManager();
        $projet = $em->getRepository(Projet::class)->find($idProjet);
        $sprint = $em->getRepository(Sprint::class)->find($idSprint);


        $story = new UserStory();
        $story->setNomstory($name);
        $story->setBv($bv);
        $story->setC($cap);
        $story->setPriorite($priorite);
        $story->setComplexite($complexite);
        $story->setDescription($description);
        $story->setRoi($roi);
        $story->setProjet($projet);
        $story->setSprint($sprint);
        $story->setStatus($status);

        $em->persist($story);
        $em->flush();

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getSprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($story);
        return new JsonResponse($formatted);
    }

    public function UpdateStoryAction($name, $bv, $cap,$complexite,$roi,$priorite,$status,$description,$idStory)
    {

        $em = $this->getDoctrine()->getManager();
        $story = $em->getRepository(UserStory::class)->find($idStory);


        $story->setNomstory($name);
        $story->setBv($bv);
        $story->setC($cap);
        $story->setPriorite($priorite);
        $story->setComplexite($complexite);
        $story->setDescription($description);
        $story->setRoi($roi);
        $story->setStatus($status);

        $em->persist($story);
        $em->flush();

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getSprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($story);
        return new JsonResponse($formatted);

    }


    public function deleteStoryAction($id)
    {
        $em = $this->getDoctrine()->getManager();
        $sprint = $em->getRepository(UserStory::class)->find($id);
        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getSprint();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($sprint);

        $em->remove($sprint);
        $em->flush();

        return new JsonResponse($formatted);
    }
    public function countStatusAction($status)
    {
        $count = 0;
        $em = $this->getDoctrine()->getManager();
        $result = $em->getRepository(UserStory::class)->findBy(array('status' => $status));
        foreach ($result as $item) {

            $count++;
        }
        if($count > 0 )
        {
            $serializer = new Serializer([new ObjectNormalizer()]);
            $formatted = $serializer->normalize($count);
            return new JsonResponse($formatted);
        }
        else
        {
            $serializer = new Serializer([new ObjectNormalizer()]);
            $formatted = $serializer->normalize($count);
            return new JsonResponse($formatted);
        }
    }




}
