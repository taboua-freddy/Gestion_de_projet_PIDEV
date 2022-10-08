<?php

namespace TacheBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Fonctionnalite
 *
 * @ORM\Table(name="fonctionnalite", indexes={@ORM\Index(name="sprint", columns={"idSprint"})})
 * @ORM\Entity
 */
class Fonctionnalite
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
     * @var string
     *
     * @ORM\Column(name="nomFn", type="string", length=20, nullable=false)
     */
    private $nomfn;

    /**
     * @var integer
     *
     * @ORM\Column(name="priorite", type="integer", nullable=false)
     */
    private $priorite;

    /**
     * @var \Sprint
     *
     * @ORM\ManyToOne(targetEntity="SprintBundle\Entity\Sprint")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idSprint", referencedColumnName="idSprint")
     * })
     */
    private $sprint;

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
    public function getNomfn()
    {
        return $this->nomfn;
    }

    /**
     * @param string $nomfn
     */
    public function setNomfn($nomfn)
    {
        $this->nomfn = $nomfn;
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
     * @return \Sprint
     */
    public function getSprint()
    {
        return $this->sprint;
    }

    /**
     * @param \Sprint $sprint
     */
    public function setSprint($sprint)
    {
        $this->sprint = $sprint;
    }



}

