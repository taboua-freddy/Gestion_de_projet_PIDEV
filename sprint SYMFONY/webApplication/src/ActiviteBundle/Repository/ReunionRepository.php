<?php

namespace ActiviteBundle\Repository;

use ActiviteBundle\Entity\PresenceReunion;
use ActiviteBundle\Entity\Reunion;
use AppBundle\Entity\User;
use Doctrine\ORM\EntityRepository;
use SprintBundle\Entity\Sprint;

Class ReunionRepository extends EntityRepository
{
    public function findByUserId($id)
    {
        $query = $this->getEntityManager()->createQueryBuilder()
            ->select("r")
            ->from(Reunion::class,"r")
            ->innerJoin(PresenceReunion::class,"p","WITH","r.idreunion=p.reunion")
            ->innerJoin(User::class,"u","WITH","p.user=u.id")
            ->where("u.id=:id")
            ->setParameter(":id",$id)
            ->getQuery();

        return $query->getResult();
    }

    public function getSprintProjet($id)
    {
        return $this->getEntityManager()->createQueryBuilder()
            ->select("s")
            ->from(Sprint::class,"s")
            ->where("s.projet=:id")
            ->setParameter(":id",$id)
            ->orderBy("s.datedebut")
            ->getQuery()
            ->getResult();
    }

    public function getParticipantsSelected($id)
    {
        return $this->getEntityManager()->createQueryBuilder()
            ->select("u")
            ->from(User::class,"u")
            ->innerJoin(PresenceReunion::class,"p","WITH","u.id=p.user")
            ->innerJoin(Reunion::class,"r","WITH","r.idreunion=p.reunion")
            ->where("r.idreunion=:id")
            ->setParameter(":id",$id)
            ->getQuery()
            ->getResult();

    }
}