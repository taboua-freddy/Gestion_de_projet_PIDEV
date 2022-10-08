<?php

namespace ReclamationBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Reclamation
 *
 * @ORM\Table(name="reclamation", indexes={@ORM\Index(name="user", columns={"idUser"})})
 * @ORM\Entity
 */
class Reclamation
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idRec", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    public $idrec;

    /**
     * @var string
     *
     * @ORM\Column(name="description", type="string", length=20, nullable=false)
     */
    public $description;

    /**
     * @var integer
     *
     * @ORM\Column(name="etat", type="integer", nullable=false)
     */
    public $etat;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateRec", type="date", nullable=false)
     */
    public $daterec;

    /**
     * @var string
     *
     * @ORM\Column(name="reponse", type="text", length=65535, nullable=true)
     */
    public $reponse;

    /**
     * @var string
     *
     * @ORM\Column(name="typeRec", type="string", length=255, nullable=false)
     */
    public $typerec;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idUser", referencedColumnName="idUser")
     * })
     */
    public $user;

    /**
     * @return int
     */
    public function getIdrec()
    {
        return $this->idrec;
    }

    /**
     * @param int $idrec
     */
    public function setIdrec($idrec)
    {
        $this->idrec = $idrec;
    }

    /**
     * @return string
     */
    public function getDescription()
    {
        return $this->description;
    }

    /**
     * @param string $description
     */
    public function setDescription($description)
    {
        $this->description = $description;
    }

    /**
     * @return int
     */
    public function getEtat()
    {
        return $this->etat;
    }

    /**
     * @param int $etat
     */
    public function setEtat($etat)
    {
        $this->etat = $etat;
    }

    /**
     * @return \DateTime
     */
    public function getDaterec()
    {
        return $this->daterec;
    }

    /**
     * @param \DateTime $daterec
     */
    public function setDaterec($daterec)
    {
        $this->daterec = $daterec;
    }

    /**
     * @return string
     */
    public function getReponse()
    {
        return $this->reponse;
    }

    /**
     * @param string $reponse
     */
    public function setReponse($reponse)
    {
        $this->reponse = $reponse;
    }

    /**
     * @return string
     */
    public function getTyperec()
    {
        return $this->typerec;
    }

    /**
     * @param string $typerec
     */
    public function setTyperec($typerec)
    {
        $this->typerec = $typerec;
    }

    /**
     * @return \User
     */
    public function getUser()
    {
        return $this->user;
    }

    /**
     * @param \User $user
     */
    public function setUser($user)
    {
        $this->user = $user;
    }


}

