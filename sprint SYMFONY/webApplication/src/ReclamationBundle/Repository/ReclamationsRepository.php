<?php


namespace ReclamationBundle\Repository;


use Doctrine\ORM\EntityRepository;
use ReclamationBundle\Entity\Reclamation;

class ReclamationsRepository extends EntityRepository
{
    public function getReclamation(int $id)
    {
        return $this->getEntityManager()->createQueryBuilder()
            ->select("r")
            ->from(Reclamation::class,"reclamation")
            ->where('r.user = :id')
            ->getQuery()
            ->setParameter('id',$id)
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
}