package com.ifro.tcc_livraria_back.mapper

interface Mapper<T, U> {

    fun map(t: T): U

}