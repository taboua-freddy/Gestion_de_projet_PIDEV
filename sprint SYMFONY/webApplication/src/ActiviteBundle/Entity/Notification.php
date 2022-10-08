<?php

namespace ActiviteBundle\Entity;

use AppBundle\Entity\User;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use SBC\NotificationsBundle\Model\BaseNotification;

/**
 * Notification
 *
 * @ORM\Table(name="notification")
 * @ORM\Entity(repositoryClass="ActiviteBundle\Repository\NotificationRepository")
 */
class Notification extends BaseNotification implements \JsonSerializable
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
     * @var string
     *
     * @ORM\Column(name="user_enable", type="text", length=65535)
     */
    private $userEnable = "a:0:{}"; //

    /**
     * @return array
     */
    public function getUserEnable(): array
    {
        return unserialize($this->userEnable);
    }

    /**
     * @param array $userEnable
     * @return Notification
     */
    public function setUserEnable(array $userEnable): Notification
    {
        $this->userEnable = serialize($userEnable);
        return $this;
    }

    public function addUser(User $user)
    {
        $users = $this->getUserEnable();
        $admin = false;
        if(in_array("SCRUM_MASTER",$user->getRoles()))
            $admin = true;
        $users[$user->getId()]=[
            "seen"=>false,
            "admin"=>$admin,
        ];
        $this->setUserEnable($users);
    }

    /**
     * @param User|null $user
     * @return bool
     */
    public function isUserEnable(User $user)
    {
        if($user==null)
            return false;
        return key_exists($user->getId(),$this->getUserEnable());
    }

    /**
     *
     * @param User $user
     * @return bool
     */
    public function isDisplayable(User $user)
    {
        if($this->isUserEnable($user))
        {
            return !$this->getUserEnable()[$user->getId()]["seen"];
        }
        return false;
    }

    /**
     * @param User $user
     */
    public function markSeen(User $user)
    {
        $users = $this->getUserEnable();
        if($this->isUserEnable($user))
        {
            $users[$user->getId()]["seen"]=true;
        }
        $this->setUserEnable($users);
    }

    /**
     * @param User $user
     */
    public function removeUser(User $user)
    {
        $users = $this->getUserEnable();
        if($this->isUserEnable($user))
        {
            unset($users[$user->getId()]);
        }
        $this->setUserEnable($users);
    }

    public function __construct()
    {
        parent::__construct();
    }

    function jsonSerialize()
    {
        return get_object_vars($this);
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

}

