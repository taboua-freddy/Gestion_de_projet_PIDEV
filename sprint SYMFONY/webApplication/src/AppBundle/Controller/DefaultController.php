<?php

namespace AppBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;

class DefaultController extends Controller
{
    /**
     * @Route("/", name="homepage")
     */
    public function indexAction(Request $request)
    {
        $controller = $this->container->get("fos_user.security.controller");

        if(!$controller->isGranted("IS_AUTHENTICATED_FULLY"))
            return $controller->loginAction($request);

        return $this->render('default/index.html.twig',['user'=>$controller->getUser()]);
    }
}
