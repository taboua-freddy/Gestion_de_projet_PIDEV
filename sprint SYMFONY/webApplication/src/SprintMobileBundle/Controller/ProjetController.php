<?php

namespace SprintMobileBundle\Controller;

use ProjetBundle\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\HttpFoundation\Request;

class ProjetController extends Controller
{
    public function fetchProjectsAction()
    {
        $em = $this->getDoctrine()->getManager();
        $projects = $em->getRepository(Projet::class)->findAll();

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($projects);
        return new JsonResponse($formatted);
    }


}
