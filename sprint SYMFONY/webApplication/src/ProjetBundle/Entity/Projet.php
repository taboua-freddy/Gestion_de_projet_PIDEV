<?php

namespace ProjetBundle\Entity;

use DateTime;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
/**
 * Projet
 *
 * @ORM\Table(name="projet")
 * @ORM\Entity(repositoryClass="ProjetBundle\Repository\ProjetRepository")
 */
class Projet implements \JsonSerializable
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idProjet", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idprojet;

    /**
     * @var string
     * @Assert\NotBlank
     * @ORM\Column(name="nom", type="string", length=20, nullable=false)
     */
    private $nom;

    /**
     * @var DateTime
     * @Assert\Type(
     *      type = "\DateTime",
     *      message = "Start Date invalid ",
     * )

     * @ORM\Column(name="dateDebut", type="date", nullable=false)
     */
    private $datedebut;

    /**
     * @var DateTime
     * @Assert\Type(
     *      type = "\DateTime",
     *      message = "End Date invalid",
     * )
     * @Assert\GreaterThanOrEqual(
     *      value = "today",
     *      message = "Must be equal or greater than today date"
     * )
     * @Assert\Expression(
     *     "this.getDatefin() >= this.getDatedebut()",
     *     message="Must be greater than Start date .."
     * )
     * @ORM\Column(name="dateFin", type="date", nullable=false)
     */
    private $datefin;

    /**
     * @var string
     * @Assert\Length(
     *      min = 2,
     *      max = 50,
     *      minMessage = "Your first name must be at least {{ limit }} characters long",
     *      maxMessage = "Your first name cannot be longer than {{ limit }} characters"
     * )
     * @ORM\Column(name="description", type="text", length=65535, nullable=false)
     */
    private $description;

    /**
     * @var string
     *
     * @ORM\Column(name="status", type="string", length=20, nullable=false)
     */
    private $status='Notstarted';





    /**
     * @return int
     */
    public function getIdprojet()
    {
        return $this->idprojet;
    }

    /**
     * @param int $idprojet
     * @return Projet
     */
    public function setIdprojet($idprojet)
    {
        $this->idprojet = $idprojet;
        return $this;
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
     * @return Projet
     */
    public function setNom($nom)
    {
        $this->nom = $nom;
        return $this;
    }

    /**
     * @return DateTime
     */
    public function getDatedebut()
    {
        return $this->datedebut;
    }

    /**
     * @param DateTime $datedebut
     * @return Projet
     */
    public function setDatedebut($datedebut)
    {
        $this->datedebut = $datedebut;
        return $this;
    }

    /**
     * @return DateTime
     */
    public function getDatefin()
    {
        return $this->datefin;
    }

    /**
     * @param DateTime $datefin
     * @return Projet
     */
    public function setDatefin($datefin)
    {
        $this->datefin = $datefin;
        return $this;
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
     * @return Projet
     */
    public function setDescription($description)
    {
        $this->description = $description;
        return $this;
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
     * @return Projet
     */
    public function setStatus($status)
    {
        $this->status = $status;
        return $this;
    }

   

    public function __toString()
    {
        return  $this->nom;
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }

}

