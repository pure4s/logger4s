package org.pure4s.logger4s

trait Logger[F[_]] {
    def error(msg: String): F[Unit]
    def error(msg: String, err: Throwable): F[Unit]
    def warn(msg: String): F[Unit]
    def info(msg: String): F[Unit]
    def debug(msg: String): F[Unit]
}