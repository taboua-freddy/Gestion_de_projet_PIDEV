<?php

namespace ActiviteBundle\Controller;

use ActiviteBundle\Entity\Notification;
use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Annotation\Route;

class NotificationController extends Controller
{

    public function displayAction()
    {
        return $this->render(':default:notification.html.twig', array(
            'notifs' => $this->getNotifs(),
            'numberNew' => $this->CountNewNotif(),
        ));
    }

    /**
     * @Route("/notification/mark_seen")
     * @return JsonResponse
     */
    public function markSeenAction()
    {
        $em = $this->getDoctrine()->getManager();
        /** @var Notification[] $notifs */
        $notifs = $em->getRepository(Notification::class)->findAll();
        $user = $this->getUser();
        foreach ($notifs as $notif)
        {
            $notif->markSeen($user);
            $em->persist($notif);
        }
        $em->flush();
        return $this->json([
            'notifs' => $this->getNotifs(),
            'numberNew' => $this->CountNewNotif(),
        ]);
    }

    /**
     * @Route("/notification/deleteAll")
     * @return JsonResponse
     */
    public function deleteAction()
    {
        $em = $this->getDoctrine()->getManager();
        $utils = new Utils();
        /** @var Notification[] $notifs */
        $notifs = $em->getRepository(Notification::class)->findAll();
        foreach ($notifs as $notif)
        {
            $notif->removeUser($this->getUser());
            if(!$utils->isSimpleUser($this->getUser()->getRoles()))
            {
                $idUsers = array_keys($notif->getUserEnable());
                foreach ($idUsers as $idUser)
                {
                    $user = $em->getRepository(User::class)->findOneBy(["id"=>$idUser]);
                    if(!$user)
                        $notif->removeUser($user);
                }
            }
            if(empty($notif->getUserEnable()))
                $em->remove($notif);
            else
                $em->persist($notif);
        }
        $em->flush();
        return $this->json([
            'notifs' => $this->getNotifs(),
            'numberNew' => $this->CountNewNotif(),
        ]);
    }

    /**
     * @Route("/notification/getAll")
     * @return JsonResponse
     */
    public function getNotifsAction()
    {
        return $this->json([
            'notifs' => $this->getNotifs(),
            'numberNew' => $this->CountNewNotif(),
        ]);
    }

    /**
     * @return Notification[]
     */
    public function getNotifs()
    {
        /** @var Notification[] $notifs */
        $notifsUser = [];
        /** @var Notification[] $notifs */
        $notifs = $this->getDoctrine()->getRepository(Notification::class)->findAll();
        foreach ($notifs as $notif)
        {
            if($notif->isUserEnable($this->getUser()))
                array_push($notifsUser,$notif);
        }
        return array_reverse($notifsUser);
    }

    /**
     * @return int
     */
    public function CountNewNotif()
    {
        $numberNew = 0;
        $notis = $this->getNotifs();
        foreach ($notis as $notif)
        {
            if($notif->isDisplayable($this->getUser()))
                $numberNew++;
        }
        return $numberNew;
    }
}
