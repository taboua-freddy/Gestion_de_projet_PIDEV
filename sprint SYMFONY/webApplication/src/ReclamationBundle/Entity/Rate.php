<?php

namespace ReclamationBundle\Entity;

use AppBundle\Entity\User;
use Doctrine\ORM\Mapping as ORM;

/**
 * Rate
 *
 * @ORM\Table(name="rate")
 * @ORM\Entity(repositoryClass="ReclamationBundle\Repository\RateRepository")
 */
class Rate
{
    /**
     * @var int
     *
     * @ORM\Column(name="id", type="integer")
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    private $id;

    /**
     * @var int
     *
     * @ORM\Column(name="val", type="integer")
     */
    private $val;

    /**
     * @var Reclamation
     * @ORM\ManyToOne(targetEntity="ReclamationBundle\Entity\Reclamation")
     * @ORM\JoinColumn(name="idrec",referencedColumnName="idRec",onDelete="CASCADE")
     */
    private $Reclamation;

    /**
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\User")
     *
     * @ORM\JoinColumn(name="idUser",referencedColumnName="idUser")
     */
    private $User;

    /**
     * Get id
     *
     * @return int
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set val
     *
     * @param integer $val
     *
     * @return Rate
     */
    public function setval($val)
    {
        $this->val = $val;

        return $this;
    }

    /**
     * Get val
     *
     * @return int
     */
    public function getval()
    {
        return $this->val;
    }

    /**
     * @return Reclamation
     */
    public function getReclamation()
    {
        return $this->Reclamation;
    }

    /**
     * @param mixed $Reclamation
     */
    public function setReclamation($Reclamation)
    {
        $this->Reclamation = $Reclamation;
    }

    /**
     * @return User
     */
    public function getUser()
    {
        return $this->User;
    }

    /**
     * @param mixed $User
     */
    public function setUser($User)
    {
        $this->User = $User;
    }

}

