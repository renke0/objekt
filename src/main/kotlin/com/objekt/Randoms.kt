package com.objekt

fun randomString(lengthPool: Pool<Int>, charsPool: Pool<Char>): String {
  val length = lengthPool.random()
  return (1..length).map { charsPool.random() }.joinToString("")
}
