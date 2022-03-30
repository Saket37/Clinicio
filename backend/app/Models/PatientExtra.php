<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Models\PatientExtra
 *
 * @property int $id
 * @property string $blood_group
 * @property float $weight
 * @property int $patient_id
 * @property \Illuminate\Support\Carbon|null $created_at
 * @property \Illuminate\Support\Carbon|null $updated_at
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra newModelQuery()
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra newQuery()
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra query()
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra whereBloodGroup($value)
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra whereCreatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra whereId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra wherePatientId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra whereUpdatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|PatientExtra whereWeight($value)
 * @mixin \Eloquent
 */
class PatientExtra extends Model
{
    protected $fillable = [
        'blood_group',
        'weight',
        'patient_id'
    ];

    public function user(): \Illuminate\Database\Eloquent\Relations\BelongsTo
    {
        return $this->belongsTo(User::class, 'patient_id','id');
    }
}
