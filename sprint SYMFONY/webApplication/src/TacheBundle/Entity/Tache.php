<?php

namespace TacheBundle\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

/**
 * Tache
 *
 * @ORM\Table(name="tache", indexes={@ORM\Index(name="fonctionnalite", columns={"idStory"}), @ORM\Index(name="user1", columns={"idUser"})})
 * @ORM\Entity
 */
class Tache
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idTache", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idtache;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateDebut", type="date", nullable=false)
     */
    private $datedebut;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateFin", type="date", nullable=false)
     */
    private $datefin;

    /**
     * @var integer
     *
     * @ORM\Column(name="priorite", type="integer", nullable=false)
     */
    private $priorite;

    /**
     * @var string
     *
     * @ORM\Column(name="nom", type="string", length=20, nullable=false)
     */
    private $nom;

    /**
     * @var string
     *
     * @ORM\Column(name="etat", type="string", length=20, nullable=false)
     */
    private $etat;

    /**
     * @var string
     *
     * @ORM\Column(name="description", type="string", length=20000, nullable=false)
     */
    private $description;

    /**
     * @var Fonctionnalite
     *
     * @ORM\ManyToOne(targetEntity="Fonctionnalite")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idStory", referencedColumnName="idStory")
     * })
     */
    private $idstory;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idUser", referencedColumnName="idUser")
     * })
     */
    private $user;


    /**
     * Many features have one product. This is the owning side.
     * @ORM\ManyToOne(targetEntity="SprintBundle\Entity\UserStory", inversedBy="taches")
     * @ORM\JoinColumn(name="userstory_id", referencedColumnName="idStory")
     */
    private $userstory;


    /**
     * Tache constructor.
     */
    public function __construct()
    {
    }


    /**
     * @return int
     */
    public function getIdtache()
    {
        return $this->idtache;
    }

    /**
     * @param int $idtache
     */
    public function setIdtache($idtache)
    {
        $this->idtache = $idtache;
    }

    /**
     * @return \DateTime
     */
    public function getDatedebut()
    {
        return $this->datedebut;
    }

    /**
     * @param \DateTime $datedebut
     */
    public function setDatedebut($datedebut)
    {
        $this->datedebut = $datedebut;
    }

    /**
     * @return \DateTime
     */
    public function getDatefin()
    {
        return $this->datefin;
    }

    /**
     * @param \DateTime $datefin
     */
    public function setDatefin($datefin)
    {
        $this->datefin = $datefin;
    }

    /**
     * @return int
     */
    public function getPriorite()
    {
        return $this->priorite;
    }

    /**
     * @param int $priorite
     */
    public function setPriorite($priorite)
    {
        $this->priorite = $priorite;
    }

    /**
     * @return string
     */
    public function getNom()
    {
        return $this->nom;
    }

    /**
     * @param string $nom
     */
    public function setNom($nom)
    {
        $this->nom = $nom;
    }

    /**
     * @return string
     */
    public function getEtat()
    {
        return $this->etat;
    }

    /**
     * @param string $etat
     */
    public function setEtat($etat)
    {
        $this->etat = $etat;
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
     * @return \Fonctionnalite
     */
    public function getIdstory()
    {
        return $this->idstory;
    }

    /**
     * @param \Fonctionnalite $idstory
     */
    public function setIdstory($idstory)
    {
        $this->idstory = $idstory;
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


    public function __toString()
    {
        return  $this->nom ;
    }

    /**
     * @return mixed
     */
    public function getUserstory()
    {
        return $this->userstory;
    }

    /**
     * @param mixed $userstory
     */
    public function setUserstory($userstory)
    {
        $this->userstory = $userstory;
    }


}

