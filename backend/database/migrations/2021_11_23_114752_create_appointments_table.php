<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateAppointmentsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('appointments', function (Blueprint $table) {
            $table->id();
            $table->bigInteger('slot_id')->nullable(false)->unsigned();
            $table->foreign('slot_id')->references('id')->on('slots')->cascadeOnUpdate()->cascadeOnDelete();
            $table->bigInteger('booked_by' )->nullable(false)->unsigned();
            $table->foreign('booked_by')->references('id')->on('users')->cascadeOnUpdate()->cascadeOnDelete();
            $table->string('reason')->nullable(false);
            $table->text('notes')->nullable(true);
            $table->timestamps();

            $table->unique(['slot_id', 'booked_by']);
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('appointments');
    }
}
