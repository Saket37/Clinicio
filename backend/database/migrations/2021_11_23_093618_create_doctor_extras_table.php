<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

const SPECIALITY = ['General Physician', 'ENT', 'Pediatrician', 'Dentist', 'Oncologist', 'Surgeon', 'Psychiatrist', 'Dermatologist', 'Orthopedist', 'Neurologist'];
class CreateDoctorExtrasTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('doctor_extras', function (Blueprint $table) {
            $table->id();
            $table->enum('speciality', SPECIALITY)->nullable(false);
            $table->string('degree', 50)->nullable(false);
            $table->bigInteger('doctor_id')->unsigned()->nullable(false)->unique();
            $table->foreign('doctor_id')->references('id')->on('users')->cascadeOnDelete()->cascadeOnUpdate();
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
        Schema::dropIfExists('doctor_extras');
    }
}
