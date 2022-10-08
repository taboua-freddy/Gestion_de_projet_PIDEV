<?php

namespace ActiviteBundle\Controller;

use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Entity\PresenceReunion;
use ActiviteBundle\Entity\Reunion;
use ActiviteBundle\Service\TemplatesData;
use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class FiltreController extends Controller
{
    /**
     * @Route("/search_activity" ,name="search_activity")
     * @param Request $request
     * @return Response
     * @throws \Exception
     */
    public function searchAction(Request $request)
    {
        $template = new TemplatesData();
        $utils = new Utils();
        $dateDebut = $request->get("start");
        $dateFin = $request->get("end");
        $titre = $request->get("titre");
        $critere = $request->get("critere");
        $type = $request->get("type");
        $importance = $request->get("importance");
        $errors = [];
        $reunions = [];
        $events = [];

        $qb = $this->getDoctrine()->getRepository(Reunion::class)->createQueryBuilder("r")->select("r")->orderBy("r.datedebut");
        $qbe = $this->getDoctrine()->getRepository(Evenement::class)->createQueryBuilder("e")->select("e")->orderBy("e.datedebut");
        if($utils->isSimpleUser($this->getUser()->getRoles()))
        {
            $qb->innerJoin(PresenceReunion::class,"p","WITH","r.idreunion=p.reunion")
                ->innerJoin(User::class,"u","WITH","p.user=u.id")->andwhere("u.id=:id")
                ->setParameter(":id",$this->getUser()->getId());
        }
        if($critere == "bytitle")
        {
            if($type)
            {
                if(in_array("meeting",$type))
                {
                    if($importance)
                    {
                        $qb->andWhere($qb->expr()->in("r.importance",$importance));
                        // recherche par importance
                    }
                    // recherche des meetings par titre
                    $qb->andWhere('r.titre like :titre')->setParameter(":titre",'%'.$titre.'%');
                    $reunions = $qb->getQuery()->getResult();
                }
                if(in_array("evenement",$type))
                {
                    // recheche des evenments par titre
                    $qbe->where('e.titre like :titre')->setParameter(":titre",'%'.$titre.'%');
                    $events = $qbe->getQuery()->getResult();
                }
            }
        }
        elseif ($critere == "bydate")
        {
            if($type)
            {
                if ($dateDebut!="")
                {
                    if($dateFin!="")
                    {
                        //recherche par meeting par intevalle de date
                        $qbe->andWhere('e.datefin<=:fin')->setParameter(":fin",new \DateTime($dateFin));
                        $qb->andWhere('r.datefin<=:fin')->setParameter(":fin",new \DateTime($dateFin));
                    }
                    if(in_array("meeting",$type))
                    {
                        if($importance)
                        {
                            // recherche par importance
                            $qb->andWhere($qb->expr()->in("r.importance",$importance));
                        }
                        //recherche par meeting par date
                        $qb->andWhere('r.datedebut>=:debut')->setParameter(":debut",new \DateTime($dateDebut));

                        $reunions = $qb->getQuery()->getResult();
                    }
                    if(in_array("evenement",$type))
                    {
                        //recherche par evenement par date
                        $qbe->andWhere('e.datedebut>:debut')->setParameter(":debut",new \DateTime($dateDebut));
                        $events = $qbe->getQuery()->getResult();
                    }
                }
                else
                {
                    $errors = ["date"=>["vous devez au moin saisir la date de debut"]];
                }
            }
        }

        $data = $template->templateSearchMeeting($reunions) . $template->templateSearchEvent($events);

        return $this->json(["searchedItems"=>$data,"errors"=>$errors]);
    }
}
