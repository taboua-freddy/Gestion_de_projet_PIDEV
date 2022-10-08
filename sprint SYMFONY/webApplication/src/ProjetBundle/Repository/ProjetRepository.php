<?php


namespace ProjetBundle\Repository;


class ProjetRepository extends \Doctrine\ORM\EntityRepository
{

    public function Search($word){


        $qry=$this->createQueryBuilder('m')
            ->where('m.nom LIKE :param')
            ->orwhere('m.datedebut LIKE :param')
            ->orwhere('m.datefin LIKE :param')
            ->orwhere('m.description LIKE :param')
            ->orwhere('m.status LIKE :param')
            ->setParameter('param', '%'.$word.'%');
        return $qry->getQuery()->getResult();

    }
}