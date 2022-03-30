<?php

namespace App\Models;

class SlotStatus
{
    const AVAILABLE = "AVAILABLE";
    const BOOKED = "BOOKED";
    const CANCELLED = "CANCELLED";

    static function getAll(): array
    {
        return array(self::AVAILABLE, self::BOOKED, self::CANCELLED);
    }
}
