<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Models\Availability
 *
 * @property int $id
 * @property string $day
 * @property string $start_time
 * @property string $end_time
 * @property int $interval
 * @property int $doc_id
 * @property \Illuminate\Support\Carbon|null $created_at
 * @property \Illuminate\Support\Carbon|null $updated_at
 * @method static \Illuminate\Database\Eloquent\Builder|Availability newModelQuery()
 * @method static \Illuminate\Database\Eloquent\Builder|Availability newQuery()
 * @method static \Illuminate\Database\Eloquent\Builder|Availability query()
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereCreatedAt($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereDay($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereDocId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereEndTime($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereId($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereInterval($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereStartTime($value)
 * @method static \Illuminate\Database\Eloquent\Builder|Availability whereUpdatedAt($value)
 * @mixin \Eloquent
 */
class Availability extends Model
{
    protected $fillable = [
        'day',
        'date',
        'start_time',
        'end_time',
        'interval',
        'doc_id'
    ];

    public function user(): \Illuminate\Database\Eloquent\Relations\BelongsTo
    {
        return $this->belongsTo(User::class, 'doc_id', 'id');
    }
}
