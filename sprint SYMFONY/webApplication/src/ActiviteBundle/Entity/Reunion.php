<?php

namespace ActiviteBundle\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use SBC\NotificationsBundle\Builder\NotificationBuilder;
use SBC\NotificationsBundle\Model\NotifiableInterface;
use Symfony\Component\Validator\Constraints as Assert;
use ActiviteBundle\Service\Utils;

/**
 * Reunion
 *
 * @ORM\Table(name="reunion", indexes={@ORM\Index(name="user3", columns={"Coordonateur"}), @ORM\Index(name="sprint1", columns={"idSprint"})})
 * @ORM\Entity(repositoryClass="ActiviteBundle\Repository\ReunionRepository")
 */
class Reunion implements NotifiableInterface , \JsonSerializable
{

    /**
     * @var integer
     *
     * @ORM\Column(name="idReunion", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idreunion;

    /**
     * @var string
     * @Assert\NotBlank(message="vous devez donner un titre de réunion")
     * @ORM\Column(name="titre", type="string", length=60, nullable=false)
     */
    private $titre;

    /**
     * @var \DateTime
     * @Assert\NotBlank(message="la date de debut ne peut être vide")
     * @ORM\Column(name="dateDebut", type="datetime", nullable=false)
     *
     */
    private $datedebut;

    /**
     * @var \DateTime
     * @Assert\GreaterThan(propertyPath="dateDebut")
     * @Assert\NotBlank(message="la date de fin ne peut être vide")
     * @ORM\Column(name="dateFin", type="datetime", nullable=false)
     */
    private $datefin;

    /**
     * @var string
     *
     * @ORM\Column(name="description", type="text", length=65535, nullable=true)
     */
    private $description;

    /**
     * @var string
     *
     * @ORM\Column(name="themeDuJour", type="string", length=255, nullable=true)
     */
    private $themedujour;

    /**
     * @var integer
     *
     * @ORM\Column(name="importance", type="integer", nullable=false)
     */
    private $importance;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateRappel", type="datetime", nullable=true)
     */
    private $daterappel;

    /**
     * @var \Sprint
     *
     * @ORM\ManyToOne(targetEntity="SprintBundle\Entity\Sprint")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idSprint", referencedColumnName="idSprint",onDelete="SET NULL")
     * })
     */
    private $sprint;

    /**
     * @var \User
     *
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="Coordonateur", referencedColumnName="idUser",onDelete="SET NULL")
     * })
     */
    private $coordonateur;

    /**
     * @var Collection
     *
     * @ORM\OneToMany(targetEntity="ActiviteBundle\Entity\PresenceReunion", mappedBy="reunion" , cascade={"persist"})
     *
     */
    private $presenceReunion;

    /**
     * @var array|mixed
     */
    private $usersNotification;

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->presenceReunion = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * @return int
     */
    public function getIdreunion()
    {
        return $this->idreunion;
    }

    /**
     * @param int $idreunion
     * @return Reunion
     */
    public function setIdreunion($idreunion)
    {
        $this->idreunion = $idreunion;
        return $this;
    }

    /**
     * @return string
     */
    public function getTitre()
    {
        return $this->titre;
    }

    /**
     * @param string $titre
     * @return Reunion
     */
    public function setTitre($titre)
    {
        $this->titre = $titre;
        return $this;
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
     * @return Reunion
     */
    public function setDatedebut($datedebut)
    {
        $this->datedebut = $datedebut;
        return $this;
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
     * @return Reunion
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
     * @return Reunion
     */
    public function setDescription($description)
    {
        $this->description = $description;
        return $this;
    }

    /**
     * @return string
     */
    public function getThemedujour()
    {
        return $this->themedujour;
    }

    /**
     * @param string $themedujour
     * @return Reunion
     */
    public function setThemedujour($themedujour)
    {
        $this->themedujour = $themedujour;
        return $this;
    }

    /**
     * @return string
     */
    public function getImportance()
    {
        return $this->importance;
    }

    /**
     * @param string $importance
     * @return Reunion
     */
    public function setImportance($importance)
    {
        $this->importance = $importance;
        return $this;
    }

    /**
     * @return \DateTime
     */
    public function getDaterappel()
    {
        return $this->daterappel;
    }

    /**
     * @param \DateTime $daterappel
     * @return Reunion
     */
    public function setDaterappel($daterappel)
    {
        $this->daterappel = $daterappel;
        return $this;
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
     * @return Reunion
     */
    public function setSprint($sprint)
    {
        $this->sprint = $sprint;
        return $this;
    }

    /**
     * @return \User
     */
    public function getCoordonateur()
    {
        return $this->coordonateur;
    }

    /**
     * @param \User $coordonateur
     * @return Reunion
     */
    public function setCoordonateur($coordonateur)
    {
        $this->coordonateur = $coordonateur;
        return $this;
    }

    /**
     * @return Collection
     */
    public function getPresenceReunion()
    {
        return $this->presenceReunion;
    }

    /**
     * @param Collection $presenceReunion
     * @return Reunion
     */
    public function setPresenceReunion($presenceReunion)
    {
        $this->presenceReunion = $presenceReunion;
        return $this;
    }


    /**
     * @inheritDoc
     */
    public function notificationsOnCreate(NotificationBuilder $builder)
    {
        $notif = new Notification();
        $notif
            ->setTitle('Réunion '.$this->getTitre())
            ->setRoute('display_meeting')
            ->setIcon("bg-success")
            ->setDescription("Nouvelle réunion programmée");
        foreach ($this->usersNotification as $user)
        {
            $notif->addUser($user);
        }
        $builder->addNotification($notif);
        return $builder;
    }

    /**
     * @inheritDoc
     */
    public function notificationsOnUpdate(NotificationBuilder $builder)
    {
        $notif = new Notification();
        $notif
            ->setTitle('Réunion '.$this->getTitre())
            ->setRoute('display_meeting')
            ->setIcon("bg-warning")
            ->setDescription("La réunion a été mise à jour");
        foreach ($this->usersNotification as $user)
        {
            $notif->addUser($user);
        }
        $builder->addNotification($notif);
        return $builder;
    }

    /**
     * @inheritDoc
     */
    public function notificationsOnDelete(NotificationBuilder $builder)
    {
        $notif = new Notification();
        $notif
            ->setTitle('Réunion '.$this->getTitre())
            ->setRoute('display_meeting')
            ->setIcon("bg-danger")
            ->setDescription("La réunion a été supprimée");
        foreach ($this->usersNotification as $user)
        {
            $notif->addUser($user);
        }
        $builder->addNotification($notif);
        return $builder;
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }

    /**
     * @param array|mixed $usersNotification
     * @return Reunion
     */
    public function setUsersNotification($usersNotification)
    {
        $this->usersNotification = $usersNotification;
        return $this;
    }
}

