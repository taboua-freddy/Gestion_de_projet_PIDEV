<?php


namespace AppBundle\Controller;


use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Bundle\WebProfilerBundle\Controller\ExceptionController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Debug\FileLinkFormatter;
use Symfony\Component\HttpKernel\Profiler\Profiler;
use Twig\Environment;

class CustomExceptionController extends Controller
{
    private $debug;
    public function __construct($debug)
    {
        $this->debug = $debug;
    }

    public function showAction()
    {
        return $this->render("@Twig/Exception/error.html.twig");
    }
}