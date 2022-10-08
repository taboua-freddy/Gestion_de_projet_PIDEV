<?php



namespace AppBundle\Repository;

use AppBundle\Entity\User;
use Doctrine\ORM\EntityRepository;
use ProjetBundle\Entity\GroupeTravail;

Class UserRepository extends EntityRepository
{

    /*public function groupByGroupe()
    {
        return [];
         $result = $this->getEntityManager()->createQueryBuilder()
            ->select("u,g")
            ->from(User::class,"u")
            ->innerJoin(GroupeTravail::class,"g",'WITH',"g.idgroupe=u.groupe")
            ->orderBy("u.nom,u.prenom")
            ->groupBy("g.idgroupe")
            ->getQuery()
            ->getResult();
         $result2 = $this->getEntityManager()->createQueryBuilder()->select("u")->from(User::class,"u")->orderBy("u.nom,u.prenom")->getQuery()->getResult();
         return array_values(array_unique(array_merge($result,$result2)));
    }*/

    public function groupByGroupeAndId($id)
    {
        $result = $this->getEntityManager()->createQueryBuilder()
            ->select("g")
            ->from(User::class,"u")
            ->orderBy("u.nom,u.prenom")
            ->Where("u.id=:id")
            ->setParameter(":id",$id)
            ->getQuery()
            ->getResult();
        return $result;
    }

    /*$result = $this->getEntityManager()->createQueryBuilder()
            ->select("g")
            ->from(User::class,"u")
            ->innerJoin(GroupeTravail::class,"g",'WITH',"g.idgroupe=u.groupe")
            ->orderBy("u.nom,u.prenom")
            ->groupBy("g.idgroupe")
            ->Where("u.id=:id")
            ->setParameter(":id",$id)
            ->getQuery()
            ->getResult();
    */


}
