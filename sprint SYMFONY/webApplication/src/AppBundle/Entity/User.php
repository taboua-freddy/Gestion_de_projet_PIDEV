<?php

namespace AppBundle\Entity;

use Doctrine\Common\Collections\Collection;
use FOS\UserBundle\Model\User as BaseUser;
use Doctrine\ORM\Mapping as ORM;
use ProjetBundle\Entity\GroupeTravail;

/**
 * User
 *
 * @ORM\Table(name="user")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\UserRepository")
 */
class User extends BaseUser implements \JsonSerializable
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idUser", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $id;

    /**
     * @var string
     *
     * @ORM\Column(name="nom", type="string", length=20, nullable=true)
     */
    private $nom;

    /**
     * @var string
     *
     * @ORM\Column(name="prenom", type="string", length=20, nullable=true)
     */
    private $prenom;

    /**
     * @var string
     *
     * @ORM\Column(name="typeuser", type="string", length=255, nullable=true)
     */
    private $typeuser;

    /**
     * @var Collection|GroupeTravail[]
     *
     * @ORM\ManyToMany(targetEntity="ProjetBundle\Entity\GroupeTravail", inversedBy="users" , cascade={"persist","merge"})
     * @ORM\JoinTable(
     *  name="user_usergroup",
     *  joinColumns={@ORM\JoinColumn(name="user_id", referencedColumnName="idUser",nullable=true)
     *
     *
     *  },
     *  inverseJoinColumns={
     *       @ORM\JoinColumn(name="usergroup_id", referencedColumnName="idGroupe")
     *  }
     * )
     */
    protected $groupe;
    /**
     * @var \Doctrine\Common\Collections\Collection
     *
     * @ORM\OneToMany(targetEntity="ActiviteBundle\Entity\PresenceReunion", mappedBy="user" , cascade={"persist"})
     *
     */
    private $presenceReunion;


    /**
     * Constructor
     */
    public function __construct()
    {
        parent::__construct();
        $this->presenceReunion = new \Doctrine\Common\Collections\ArrayCollection();
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
     * @return User
     */
    public function setNom($nom)
    {
        $this->nom = $nom;
        return $this;
    }

    /**
     * @return string
     */
    public function getPrenom()
    {
        return $this->prenom;
    }

    /**
     * @param string $prenom
     * @return User
     */
    public function setPrenom($prenom)
    {
        $this->prenom = $prenom;
        return $this;
    }

    /**
     * @return string
     */
    public function getTypeuser()
    {
        return $this->typeuser;
    }

    /**
     * @param string $typeuser
     * @return User
     */
    public function setTypeuser($typeuser)
    {
        $this->typeuser = $typeuser;
        return $this;
    }

    /**
     * @param GroupeTravail $userGroup
     */
    public function addUserGroup(GroupeTravail $userGroup)
    {
        if ($this->groupe->contains($userGroup)) {
            return;
        }

        $this->groupe->add($userGroup);
        $userGroup->addUser($this);
    }

    /**
     * @param GroupeTravail $userGroup
     */
    public function removeUserGroup(GroupeTravail $userGroup)
    {
        if (!$this->groupe->contains($userGroup)) {
            return;
        }

        $this->groupe->removeElement($userGroup);
        $userGroup->removeUser($this);
    }

    public function getGroupe() : Collection
    {
        return $this->groupe;
    }

    /**
     * @param \GroupeTravail $groupe
     * @return User
     */
    public function setGroupe($groupe)
    {
        $this->groupe = $groupe;
        return $this;
    }

    /**
     * @return int
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param int $id
     * @return User
     */
    public function setId($id)
    {
        $this->id = $id;
        return $this;
    }

    /**
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getPresenceReunion()
    {
        return $this->presenceReunion;
    }

    /**
     * @param \Doctrine\Common\Collections\Collection $presenceReunion
     * @return User
     */
    public function setPresenceReunion($presenceReunion)
    {
        $this->presenceReunion = $presenceReunion;
        return $this;
    }


    public function __toString()
    {
        return  $this->nom . " " . $this->prenom;
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }

}
