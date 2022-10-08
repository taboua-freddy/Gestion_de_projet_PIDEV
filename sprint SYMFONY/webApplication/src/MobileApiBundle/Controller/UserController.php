<?php

namespace MobileApiBundle\Controller;

use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * Class UserController
 * @Route("/user")
 * @package MobileApiBundle\Controller
 */
class UserController extends Controller
{
    /**
     * @Route("/login/{username}/{password}")
     * @param $username
     * @param $password
     * @return JsonResponse
     */
    public function indexAction($username,$password)
    {
        $data = [];
        $user = $this->getDoctrine()->getRepository(User::class)->findOneBy(["username"=>$username]);
        if($user!=null)
        {
            $encoder_service = $this->get('security.encoder_factory');
            $encoder = $encoder_service->getEncoder($user);
            $valide = $encoder->isPasswordValid($user->getPassword(), $password, $user->getSalt());
            if($valide)
                array_push($data,$user);
        }
        return $this->json($data);
    }

    /**
     * @Route("/getRole/{id}")
     * @param User $user
     * @return Response
     */
    public function getRoleAction(User $user)
    {
        $response = (new Utils())->isSimpleUser($user->getRoles())?"simple_user":"admin";

        return $this->json($response);
    }

}
