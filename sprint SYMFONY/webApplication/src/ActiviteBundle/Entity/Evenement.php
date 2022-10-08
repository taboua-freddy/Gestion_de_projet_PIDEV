<?php

namespace ActiviteBundle\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use Exception;
use ProjetBundle\Entity\Projet;
use SBC\NotificationsBundle\Builder\NotificationBuilder;
use SBC\NotificationsBundle\Model\NotifiableInterface;
use Symfony\Component\HttpFoundation\File\File;
use Vich\UploaderBundle\Mapping\Annotation as Vich;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Evenement
 *
 * @ORM\Table(name="evenement")
 * @ORM\Entity(repositoryClass="ActiviteBundle\Repository\EventsRepository")
 * @Vich\Uploadable
 */
class Evenement implements NotifiableInterface , \JsonSerializable
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idEvent", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idevent;

    /**
     * @var string
     * @Assert\NotBlank(message="vous devez donner un titre d'événement")
     * @ORM\Column(name="titre", type="string", length=255, nullable=false)
     */
    private $titre;

    /**
     * @var \DateTime
     * @Assert\NotBlank(message="la date de debut ne peut être vide")
     * @ORM\Column(name="dateDebut", type="datetime", nullable=false)
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
     * @ORM\Column(name="lieu", type="string", length=20, nullable=true)
     */
    private $lieu;

    /**
     * @var string
     *
     * @ORM\Column(name="affiche", type="blob", nullable=true)
     */
    private $affiche;

    /**
     * NOTE: This is not a mapped field of entity metadata, just a simple property.
     *
     * @Vich\UploadableField(mapping="event_image", fileNameProperty="imageName", size="imageSize")
     *
     * @var File|null
     */
    private $imageFile;

    /**
     * @ORM\Column(type="string" , nullable=true)
     *
     * @var string|null
     */
    private $imageName;

    /**
     * @ORM\Column(type="integer" , nullable=true)
     *
     * @var int|null
     */
    private $imageSize;

    /**
     * @ORM\Column(type="datetime" , nullable=true)
     *
     * @var \DateTimeInterface|null
     */
    private $updatedAt;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="dateRappel", type="datetime", nullable=true)
     */
    private $daterappel;

    /**
     * @var \Doctrine\Common\Collections\Collection
     *
     * @ORM\ManyToMany(targetEntity="ProjetBundle\Entity\Projet", inversedBy="idprojet",cascade={"persist"})
     * @ORM\JoinTable(name="expose_projet",
     *   joinColumns={
     *     @ORM\JoinColumn(name="idEvent", referencedColumnName="idEvent" ,onDelete="CASCADE")
     *   },
     *   inverseJoinColumns={
     *     @ORM\JoinColumn(name="idProjet", referencedColumnName="idProjet" ,onDelete="CASCADE")
     *   }
     * )
     */
    private $projets;

    /**
     * @var array|mixed
     */
    private $usersNotification;

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->projets = new \Doctrine\Common\Collections\ArrayCollection();
        $this->updatedAt = new \DateTime();
    }


    /**
     * @return int
     */
    public function getIdevent()
    {
        return $this->idevent;
    }

    /**
     * @param int $idevent
     * @return Evenement
     */
    public function setIdevent($idevent)
    {
        $this->idevent = $idevent;
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
     * @return Evenement
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
     * @return Evenement
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
     * @return Evenement
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
     * @return Evenement
     */
    public function setDescription($description)
    {
        $this->description = $description;
        return $this;
    }

    /**
     * @return string
     */
    public function getLieu()
    {
        return $this->lieu;
    }

    /**
     * @param string $lieu
     * @return Evenement
     */
    public function setLieu($lieu)
    {
        $this->lieu = $lieu;
        return $this;
    }

    /**
     * @return string
     */
    public function getAffiche()
    {
        return $this->affiche;
    }

    /**
     * @param string $affiche
     * @return Evenement
     */
    public function setAffiche($affiche)
    {
        $this->affiche = $affiche;
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
     * @return Evenement
     */
    public function setDaterappel($daterappel)
    {
        $this->daterappel = $daterappel;
        return $this;
    }

    /**
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getProjets()
    {
        return $this->projets;
    }

    /**
     * @param \Doctrine\Common\Collections\Collection $projets
     * @return Evenement
     */
    public function setProjets($projets)
    {
        $this->projets = $projets;
        return $this;
    }

    public function addProjet(Projet $projet)
    {
        if(!$this->projets->contains($projet))
        {
            $this->projets[]=$projet;
            //$projet->setEvents($this->);
        }
        return $this;
    }
    public function removeProjet(Projet $projet)
    {
        if($this->projets->contains($projet))
        {
            $this->projets->removeElement($projet);
            if($projet->getEvents() === $this)
            {
                $projet->setEvents(null);
            }
        }
        return $this;
    }
    public function removeProjets()
    {
        $this->projets = new ArrayCollection();
        return $this;
    }

    /**
     * If manually uploading a file (i.e. not using Symfony Form) ensure an instance
     * of 'UploadedFile' is injected into this setter to trigger the update. If this
     * bundle's configuration parameter 'inject_on_load' is set to 'true' this setter
     * must be able to accept an instance of 'File' as the bundle will inject one here
     * during Doctrine hydration.
     *
     * @param File|\Symfony\Component\HttpFoundation\File\UploadedFile|null $imageFile
     * @return Evenement
     * @throws Exception
     */
    public function setImageFile(File $imageFile = null)
    {
        $this->imageFile = $imageFile;

        if (null !== $imageFile) {
            // It is required that at least one field changes if you are using doctrine
            // otherwise the event listeners won't be called and the file is lost
            $this->updatedAt = new \DateTimeImmutable();
        }
        return $this;
    }

    public function getImageFile()
    {
        return $this->imageFile;
    }

    /**
     * @param string $imageName
     * @return Evenement
     */
    public function setImageName($imageName)
    {
        $this->imageName = $imageName;
        return $this;
    }

    public function getImageName()
    {
        return $this->imageName;
    }

    /**
     * @param int $imageSize
     * @return Evenement
     */
    public function setImageSize($imageSize)
    {
        $this->imageSize = $imageSize;
        return $this;
    }

    public function getImageSize()
    {
        return $this->imageSize;
    }

    /**
     * @inheritDoc
     */
    public function notificationsOnCreate(NotificationBuilder $builder)
    {
        $notif = new Notification();
        $notif
            ->setTitle('Evenement '.$this->getTitre())
            ->setRoute('display_meeting')
            ->setIcon("bg-success")
            ->setDescription("L'evenement a été Ajouté");
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
            ->setTitle('Evenement '.$this->getTitre())
            ->setRoute('display_meeting')
            ->setIcon("bg-warning")
            ->setDescription("L'evenement a été mise à jour");
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
            ->setTitle('Evenement '.$this->getTitre())
            ->setRoute('display_meeting')
            ->setIcon("bg-danger")
            ->setDescription("L'evenement a été supprimé");
        foreach ($this->usersNotification as $user)
        {
            $notif->addUser($user);
        }
        $builder->addNotification($notif);
        return $builder;
    }

    /**
     * @param array|mixed $usersNotification
     * @return Evenement
     */
    public function setUsersNotification($usersNotification)
    {
        $this->usersNotification = $usersNotification;
        return $this;
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }

}
