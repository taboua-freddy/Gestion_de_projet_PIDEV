<?php


namespace ActiviteBundle\Repository;


use ActiviteBundle\Entity\Evenement;
use Doctrine\ORM\EntityRepository;
use ProjetBundle\Entity\Projet;

class EventsRepository extends EntityRepository
{
    public function getProjet($idEvent)
    {
        return $this->getEntityManager()->createQueryBuilder()
            ->select("p")
            ->from(Projet::class,"projet")
            ->innerJoin(Evenement::class,"event","WITH","projet.idprojet=event.idevent")
            ->getQuery()
            ->getResult();
    }

}