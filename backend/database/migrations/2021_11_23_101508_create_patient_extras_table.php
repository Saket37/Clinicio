<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

const BLOOD_GROUP = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];
class CreatePatientExtrasTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('patient_extras', function (Blueprint $table) {
            $table->id();
            $table->enum('blood_group', BLOOD_GROUP)->nullable(false);
            $table->float('weight')->nullable(false);
            $table->bigInteger('patient_id')->unsigned()->nullable(false)->unique();
            $table->foreign('patient_id')->references('id')->on('users')->cascadeOnDelete()->cascadeOnUpdate();
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
        Schema::dropIfExists('patient_extras');
    }
}
