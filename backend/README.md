# Clinicio

This is the backend for the Clinicio Android App.

It is built using PHP and uses [Laravel Framework](https://laravel.com).

## Table of Contents

- [Prerequisite](#prerequisite)
- [Installation](#installation)
- [Configuration](#configuration)
- [APIs](#apis)

<a id = 'prerequisite'></a>

## Prerequisite

You should ensure that your web server has the following minimum PHP version and extensions:

- PHP >= 8.0
- MySQL
- BCMath PHP Extension
- Ctype PHP Extension
- Fileinfo PHP Extension
- JSON PHP Extension
- Mbstring PHP Extension
- OpenSSL PHP Extension
- PDO PHP Extension
- Tokenizer PHP Extension
- XML PHP Extension

<a id= 'installation'></a>

## Installation

Clone the GitHub Repository.

You will need to install [Composer](https://getcomposer.org/) following these instructions.

Then, simply run the following command:

```shell
composer install
``` 

<a id = 'configuration'></a>

## Configuration

Make sure you make a file named `.env` and copy the contents of `.env.example` and change

```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=clinicio
DB_USERNAME=root
DB_PASSWORD=
```

After making changes to `.env` file, then run the migrations:

```shell
php artisan migrate
```

When the migration is successful, run the PHP server:

<a id = 'php-server'></a>

```shell
php artisan serve
```

The server runs on `http://localhost:8000/`.

<a id = 'apis'></a>

## APIs

To make successful API calls, check [API Documentation](https://documenter.getpostman.com/view/18791869/UVRBnSGX).
