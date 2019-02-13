package org.pure4s.logger4s

import cats.Show

trait GLogger[F[_]] {

  def error[A: Show](msg: A): F[Unit]
  def error[A: Show](msg: A, err: Throwable): F[Unit]
  def warn[A: Show](msg: A): F[Unit]
  def info[A: Show](msg: A): F[Unit]
  def debug[A: Show](msg: A): F[Unit]

}
