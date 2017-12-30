package com.zigzag.whar.di

/**
 * Created by salah on 21/12/17.
 */

import javax.inject.Scope

@Scope
@Retention
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class FragmentScoped