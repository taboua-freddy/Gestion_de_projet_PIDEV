<?php

namespace SprintBundle\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Sprint
 *
 * @ORM\Table(name="sprint", indexes={@ORM\Index(name="projet2", columns={"idProjet"})})
 * @ORM\Entity
 */
class Sprint implements \JsonSerializable
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idSprint", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idsprint;

    /**
     * @var string
     * @Assert\NotBlank(message="Veuillez fournir le nom du sprint")
     * @ORM\Column(name="nomSprint", type="string", length=255, nullable=false)
     */
    private $nomsprint;

    /**
     * @var \DateTime
     *@var string A "Y-m-d H:i:s" formatted value
     * @ORM\Column(name="dateDebut", type="date", nullable=false)
     */
    private $datedebut;

    /**
     * @var \DateTime
     * @var string A "Y-m-d H:i:s" formatted value
     * @Assert\GreaterThan(propertyPath="datedebut")
     * @ORM\Column(name="dateFin", type="date", nullable=false)
     */
    private $datefin;

    /**
     * @var \Projet
     * @Assert\NotBlank(message="Veuillez choisir le projet auquel appartient ce sprint")
     * @ORM\ManyToOne(targetEntity="ProjetBundle\Entity\Projet")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idProjet", referencedColumnName="idProjet")
     * })
     */
    private $projet;


    /**
     * One product has many features. This is the inverse side.
     * @ORM\OneToMany(targetEntity="SprintBundle\Entity\UserStory", mappedBy="sprint")
     */
    private $userstories;


    /**
     * @return int
     */
    public function getIdsprint()
    {
        return $this->idsprint;
    }
    public function getSprint()
    {
        return $this->idsprint;
    }


    /**
     * @param int $idsprint
     */
    public function setIdsprint($idsprint)
    {
        $this->idsprint = $idsprint;
    }

    /**
     * @return string
     */
    public function getNomsprint()
    {
        return $this->nomsprint;
    }

    /**
     * @param string $nomsprint
     */
    public function setNomsprint($nomsprint)
    {
        $this->nomsprint = $nomsprint;
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
     * @return \Projet
     */
    public function getProjet()
    {
        return $this->projet;
    }

    /**
     * @param \Projet $projet
     */
    public function setProjet($projet)
    {
        $this->projet = $projet;
    }

    public function __toString()
    {
        return $this->nomsprint;
    }

    /**
     * @return mixed
     */
    public function getUserstories()
    {
        return $this->userstories;
    }

    /**
     * @param mixed $userstories
     */
    public function setUserstories($userstories)
    {
        $this->userstories = $userstories;
    }

    /**
     * @param mixed $userstories
     */
    public function addUserstory($userstories)
    {
        $this->userstories->add($userstories);
    }

    /**
     * @param mixed $userstories
     */
    public function removeUserstory($userstories)
    {
        $this->userstories->removeElement($userstories);
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }


}

