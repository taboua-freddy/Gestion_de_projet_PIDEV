<?php


namespace ProjetBundle\Repository;

use AppBundle\Entity;
class GroupeTravailRepository extends \Doctrine\ORM\EntityRepository
{
    public function findByRole($role)
    {
        $qb = $this->_em->createQueryBuilder();
        $qb->select('u')
            ->from("AppBundle:User", 'u')
            ->where('u.roles LIKE :roles')
            ->setParameter('roles', '%"'.$role.'"%');

        return $qb->getQuery()->getResult();
    }
    public function Search($word){


        $qry=$this->createQueryBuilder('m')
            ->where('m.nom LIKE :param')

            ->setParameter('param', '%'.$word.'%');
        return $qry->getQuery()->getResult();

    }
}