<?php

namespace ActiviteBundle\Entity;

use ActiviteBundle\Service\Utils;
use AppBundle\Entity\User;
use Doctrine\ORM\Mapping as ORM;

/**
 * PresenceReunion
 *
 * @ORM\Table(name="participe_reunion")
 * @ORM\Entity(repositoryClass="ActiviteBundle\Repository\PresenceReunionRepository")
 */
class PresenceReunion implements \JsonSerializable
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
     * @var User
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\User", inversedBy="presenceReunion",cascade={"persist"})
     * @ORM\JoinColumn(name="idUser", referencedColumnName="idUser",onDelete="CASCADE")
     */
    private $user;

    /**
     * @var Reunion
     * @ORM\ManyToOne(targetEntity="ActiviteBundle\Entity\Reunion", inversedBy="presenceReunion",cascade={"persist"})
     * @ORM\JoinColumn(name="idReunion", referencedColumnName="idReunion" ,onDelete="CASCADE")
     */
    private $reunion;


    /**
     * @var int
     *
     * @ORM\Column(name="presence", type="integer" )
     */
    private $presence;

    /**
     * PresenceReunion constructor.
     * @param User $user
     * @param Reunion $reunion
     * @param int $presence
     */
    public function __construct(User $user, Reunion $reunion, $presence)
    {
        $this->user = $user;
        $this->reunion = $reunion;
        $this->presence = $presence;
    }

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
     * @return User
     */
    public function getUser()
    {
        return $this->user;
    }

    /**
     * @param User $user
     * @return PresenceReunion
     */
    public function setUser($user)
    {
        $this->user = $user;
        return $this;
    }

    /**
     * @return Reunion
     */
    public function getReunion()
    {
        return $this->reunion;
    }

    /**
     * @param Reunion $reunion
     * @return PresenceReunion
     */
    public function setReunion($reunion)
    {
        $this->reunion = $reunion;
        return $this;
    }

    /**
     * @return integer
     */
    public function getPresence()
    {
        return $this->presence;
    }

    /**
     * @param string $presence
     * @return PresenceReunion
     */
    public function setPresence($presence)
    {
        $this->presence = $presence;
        return $this;
    }

    public function __toString()
    {
        return ($this->presence = 1)?"Present":"Absent";
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }

}

