# glc
gravitational microlensing simulation framework

Gravitational lensing is visual effect when gravitational field of galaxy disturb light from far remote source, usually quasar.
It leads to appearens of two or more images of quasar.
But separate stars from lensing galaxy have own impact in image, this called gravitational microlensing.

Code can be run as console application (class ConsoleApp) or web applicaiton (class MainView)
To build single jar for web run 'build_prod.bat'

Parameters of modelling:

Source - type of remote source (Flat, Gaussian, Exponential, Limb, Accretion Disk)
Source size - in RE (Einstein Radius)
Size - image area size in RE
Calculation Mode - FFC (brute force ray-shooting), HFC (hierarchical tree mode), SSD, One-Grav, Witt)
SigmaC and Gamma - background gravitational field parameters
NG - number of gravitators
Star Mass - mass of single gravitator (in solar mass)




