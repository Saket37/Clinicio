<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Models\DoctorExtra
 *
 * @property int $id
 * @property string $speciality
 * @property string $degree
 * @property int $doctor_id
 * @property \Illuminate\Support\Carbon|null $created_at
 * @property \Illuminate\Support\Carbon|null $updated_at
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra newModelQuery()
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra newQuery()
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra query()
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra whereCreatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra whereDegree($value)
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra whereDoctorId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra whereId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra whereSpeciality($value)
 * @method static \Illuminate\Database\Eloquent\Builder|DoctorExtra whereUpdatedAt($value)
 * @mixin \Eloquent
 */
class DoctorExtra extends Model
{


    protected $fillable = [
        'speciality',
        'degree',
        'doctor_id'
    ];

    public function user(): \Illuminate\Database\Eloquent\Relations\BelongsTo
    {
        return $this->belongsTo(User::class,'doctor_id','id');
    }

}
