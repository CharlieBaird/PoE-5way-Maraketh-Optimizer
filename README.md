# PoE Legion Optimizer

Quick video demo: https://youtu.be/qGzFQCBwD2Q

## Why?

I threw this together to help myself farm Stasis Prison in SSF. It helps you maximize the number of kills on a specific boss in the 5 way, thus maximizing the chance of dropping that boss' reward.

For example, if I ran a 5 way with 1 unrelenting maraketh emblem and 4 normal emblems, I would want to optimize the number of maraketh kills to have the highest chance of dropping a Stasis Prison.

The same theory applies to normal timeless jewels-- for example, if you want a lethal pride at league start you would want to optimize the number of Karui (Hyrri) kills.

## How to use

1) Download the latest .jar in the releases section
2) Open the .jar (you will need java installed)
3) Move the window to the desired location by left clicking and dragging
4) Select the boss you want to target farm (E for eternal, K for karui, V for vaal, T for templar, M for maraketh)
5) If it says "ready," you're good to go. Open the 5 way and run it.

## Quick note

This program uses the client.txt file to monitor the deaths of the bosses. It assumes the file is located at `"/Program Files (x86)/Grinding Gear Games/Path of Exile/logs/client.txt"` - if it is not, the program will probably crash. If I have time I might fix this to be configurable, but not sure if needed.
