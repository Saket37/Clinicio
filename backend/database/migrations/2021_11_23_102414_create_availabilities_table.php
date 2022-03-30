<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

const DAY = ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'];
class CreateAvailabilitiesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('availabilities', function (Blueprint $table) {
            $table->id();
            $table->enum('day', DAY)->nullable(false);
            $table->date('date')->nullable(false);
            $table->time('start_time')->nullable(false);
            $table->time('end_time')->nullable(false);
            $table->integer('interval', false, true)->nullable(false);

            $table->bigInteger('doc_id')->nullable(false)->unsigned();
            $table->foreign('doc_id')->references('id')->on('users')->cascadeOnDelete()->cascadeOnUpdate();
            $table->timestamps();
        });

    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('availabilities');

    }

}
