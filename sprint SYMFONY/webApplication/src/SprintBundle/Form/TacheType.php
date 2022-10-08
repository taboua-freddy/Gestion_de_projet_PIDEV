<?php

namespace SprintBundle\Form;

use AppBundle\Entity\User;
use Doctrine\DBAL\Types\DateType;
use FOS\CKEditorBundle\Form\Type\CKEditorType;
use SprintBundle\Entity\UserStory;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class TacheType extends AbstractType
{
    /**
     * {@inheritdoc}
     */
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('datedebut')
            ->add('datefin')
            ->add('priorite')
            ->add('nom')
            ->add('etat')
            ->add('description', CKEditorType::class, array(
                'attr' => array(
                    'class' => 'ckeditor'
                ),
            ))
            ->add('userstory', EntityType::class, [
                'class' => UserStory::class,
            ])
            ->add('user', EntityType::class, [
                'class' => User::class,
            ]);
    }/**
     * {@inheritdoc}
     */
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'TacheBundle\Entity\Tache'
        ));
    }

    /**
     * {@inheritdoc}
     */
    public function getBlockPrefix()
    {
        return 'tachebundle_tache';
    }


}
