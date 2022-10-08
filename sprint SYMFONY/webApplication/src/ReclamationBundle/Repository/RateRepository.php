<?php

namespace ReclamationBundle\Repository;


use Doctrine\ORM\EntityRepository;
use ReclamationBundle\Entity\Rate;
use ReclamationBundle\Entity\Reclamation;


class RateRepository extends EntityRepository
{
    public function getRateId($id)
    {
        return $this->getEntityManager()->createQueryBuilder()
            ->select('count(rate.val)')
            ->from(Rate::class,"rate")
            ->where("rate.Reclamation=:id AND rate.val=1")
            ->setParameter(":id",$id)
            ->getQuery()
            ->getResult();
    }
    public function getRateIdd($id)
    {
        return $this->getEntityManager()->createQueryBuilder()
            ->select('count(rate.val)')
            ->from(Rate::class,"rate")
            ->where("rate.Reclamation=:id AND rate.val=0")
            ->setParameter(":id",$id)
            ->getQuery()
            ->getResult();
    }
    public function getTab()
    {
        $con=$this->getEntityManager()->getConnection();
        $sql="select * from reclamation";
        $stnt=$con->prepare($sql);
        $stnt->execute();
        return $stnt->fetchAll();
    }
    public function getDate($dat)
    {
        $con=$this->getEntityManager()->getConnection();
        $sql="select * from reclamation WHERE dateRec=:dat";
        $stnt=$con->prepare($sql);
        $stnt->execute(["dat"=>$dat]);
        return $stnt->fetchAll();
    }
}
