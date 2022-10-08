<?php

namespace SprintBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class UserStoryType extends AbstractType
{
    /**
     * {@inheritdoc}
     */
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('nomstory', TextType::class, [
                'required' => false
            ])
            ->add('bv')
            ->add('c', NumberType::class, [
                'required' => false
            ])
            ->add('priorite')
            ->add('complexite', NumberType::class, [
                'required' => false
            ])
            ->add('status', ChoiceType::class, [
                'choices' => [
                    'TODO' => 'TODO',
                    'DOING' => 'DOING',
                    'DONE' => 'DONE',
                ]
            ])
            ->add('description')
            ->add('roi', NumberType::class, [
                'required' => true
            ])
            ->add('sprint');
    }/**
     * {@inheritdoc}
     */
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'SprintBundle\Entity\UserStory'
        ));
    }

    /**
     * {@inheritdoc}
     */
    public function getBlockPrefix()
    {
        return 'sprintbundle_userstory';
    }


}
