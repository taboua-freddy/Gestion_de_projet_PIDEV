<?php

namespace SprintBundle\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * UserStory
 *
 * @ORM\Table(name="user_story")
 * @ORM\Entity
 */
class UserStory
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idStory", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idstory;

    /**
     * @ORM\ManyToOne(targetEntity="ProjetBundle\Entity\Projet")
     * @ORM\JoinColumn(name="idProjet", referencedColumnName="idProjet")
     */
    private $projet;

    /**
     * @var string
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="nomStory", type="string", length=20, nullable=false)
     */
    private $nomstory;

    /**
     * @var integer
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="bv", type="integer", nullable=false)
     */
    private $bv;

    /**
     * @var float
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="c", type="float", precision=10, scale=0, nullable=false)
     */
    private $c;

    /**
     * @var integer
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="priorite", type="integer", nullable=false)
     */
    private $priorite;

    /**
     * @var float
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="complexite", type="float", precision=10, scale=0, nullable=false)
     */
    private $complexite;

    /**
     * @var string
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="description", type="string", length=50, nullable=false)
     */
    private $description;

    /**
     * @var float
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="ROI", type="float", precision=10, scale=0, nullable=false)
     */
    private $roi;

    /**
     * @var string
     * @Assert\NotBlank(message="Veuillez remplire ce champ")
     * @ORM\Column(name="status", type="string", length=50, nullable=false)
     */
    private $status;

    /**
     * @ORM\ManyToOne(targetEntity="Sprint", inversedBy="userstories")
     * @ORM\JoinColumn(name="sprint_id", referencedColumnName="idSprint", onDelete="SET NULL")
     */
    private $sprint;

    /**
     * @ORM\OneToMany(targetEntity="TacheBundle\Entity\Tache", mappedBy="userstory")
     */
    private $taches;



    public function __construct() {
        $this->taches = new ArrayCollection();
    }

    /**
     * @return int
     */
    public function getIdstory()
    {
        return $this->idstory;
    }

    /**
     * @param int $idstory
     */
    public function setIdstory($idstory)
    {
        $this->idstory = $idstory;
    }

    /**
     * @return string
     */
    public function getNomstory()
    {
        return $this->nomstory;
    }

    /**
     * @param string $nomstory
     */
    public function setNomstory($nomstory)
    {
        $this->nomstory = $nomstory;
    }

    /**
     * @return int
     */
    public function getBv()
    {
        return $this->bv;
    }

    /**
     * @param int $bv
     */
    public function setBv($bv)
    {
        $this->bv = $bv;
    }

    /**
     * @return float
     */
    public function getC()
    {
        return $this->c;
    }

    /**
     * @param float $c
     */
    public function setC($c)
    {
        $this->c = $c;
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
     * @return float
     */
    public function getComplexite()
    {
        return $this->complexite;
    }

    /**
     * @param float $complexite
     */
    public function setComplexite($complexite)
    {
        $this->complexite = $complexite;
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
     * @return float
     */
    public function getRoi()
    {
        return $this->roi;
    }

    /**
     * @param float $roi
     */
    public function setRoi($roi)
    {
        $this->roi = $roi;
    }

    /**
     * @return mixed
     */
    public function getProjet()
    {
        return $this->projet;
    }

    /**
     * @param mixed $projet
     */
    public function setProjet($projet)
    {
        $this->projet = $projet;
    }

    /**
     * @return mixed
     */
    public function getSprint()
    {
        return $this->sprint;
    }

    /**
     * @param mixed $sprint
     */
    public function setSprint($sprint)
    {
        $this->sprint = $sprint;
    }

    /**
     * @return string
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * @param string $status
     */
    public function setStatus(string $status)
    {
        $this->status = $status;
    }

    /**
     * @return mixed
     */
    public function getTaches()
    {
        return $this->taches;
    }

    /**
     * @param mixed $taches
     */
    public function setTaches($taches)
    {
        $this->taches = $taches;
    }

    /**
     * @param mixed $taches
     */
    public function addTache($tache)
    {
        $this->taches->add($tache);
    }
    /**
     * @param mixed $taches
     */
    public function removeTache($tache)
    {
        $this->taches->removeElement($tache);
    }



    public function __toString()
    {
        return $this->nomstory;
    }
}

