import React, { useState } from 'react';
import {
  Wrench,
  ShieldCheck,
  Star,
  Clock,
  Calendar,
  MapPin,
  Phone,
  MessageSquare,
  CheckCircle,
  Users,
  Briefcase,
  TrendingUp,
  Search,
  ChevronRight,
  Sparkles,
  ArrowLeft,
  Smartphone,
  Layers,
  FileCode,
  Download,
  AlertCircle,
  Check,
  X,
  CreditCard,
  Zap,
  HardHat,
  Droplet,
  AirVent,
  Paintbrush,
  Bell
} from 'lucide-react';

interface Provider {
  id: string;
  name: string;
  profession: string;
  experience: number;
  rating: number;
  reviews: number;
  hourlyRate: number;
  about: string;
  verified: boolean;
  serviceArea: string;
  phone: string;
  availableSlots: string[];
}

interface Booking {
  id: string;
  customerName: string;
  providerName: string;
  serviceName: string;
  date: string;
  time: string;
  address: string;
  total: number;
  status: 'Pending' | 'Confirmed' | 'Accepted' | 'On The Way' | 'Started' | 'Completed' | 'Cancelled';
}

export default function App() {
  const [activeTab, setActiveTab] = useState<'simulator' | 'admin' | 'androidide' | 'files'>('simulator');
  const [simulatorView, setSimulatorView] = useState<'home' | 'provider' | 'booking' | 'bookings_list' | 'provider_dashboard'>('home');
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [searchQuery, setSearchQuery] = useState('');

  // Sample verified providers
  const [providers, setProviders] = useState<Provider[]>([
    {
      id: 'pro_james',
      name: 'James Carter',
      profession: 'Master Electrician',
      experience: 8,
      rating: 4.9,
      reviews: 128,
      hourlyRate: 450,
      about: 'Certified master electrician with 8+ years of residential and commercial experience. Specialized in high-voltage panels, short circuit troubleshooting, and smart home automation.',
      verified: true,
      serviceArea: 'Downtown & Metro Suburbs',
      phone: '+1 (555) 234-5678',
      availableSlots: ['09:00 AM', '11:00 AM', '02:00 PM', '04:00 PM', '06:00 PM']
    },
    {
      id: 'pro_sarah',
      name: 'Sarah Miller',
      profession: 'Plumbing Specialist',
      experience: 6,
      rating: 4.8,
      reviews: 94,
      hourlyRate: 380,
      about: 'Licensed plumber handling all pipe repair, bathroom fittings, water heater installations, and emergency leakage fixes.',
      verified: true,
      serviceArea: 'North & West Metro',
      phone: '+1 (555) 345-6789',
      availableSlots: ['10:00 AM', '01:00 PM', '03:00 PM', '05:00 PM']
    },
    {
      id: 'pro_david',
      name: 'David Chen',
      profession: 'HVAC & AC Technician',
      experience: 10,
      rating: 5.0,
      reviews: 167,
      hourlyRate: 500,
      about: 'HVAC certified technician specializing in central AC diagnostics, split AC cleaning, coil replacements, and energy efficiency audits.',
      verified: true,
      serviceArea: 'South City & Central',
      phone: '+1 (555) 456-7890',
      availableSlots: ['08:00 AM', '11:00 AM', '02:00 PM', '05:00 PM']
    },
    {
      id: 'pro_michael',
      name: 'Michael Brown',
      profession: 'Appliance Repair Pro',
      experience: 4,
      rating: 4.7,
      reviews: 42,
      hourlyRate: 400,
      about: 'Experienced in refrigerator cooling issues, microwave oven magnetrons, washing machine drums, and smart home appliances.',
      verified: false,
      serviceArea: 'East Metro',
      phone: '+1 (555) 567-8901',
      availableSlots: ['09:30 AM', '01:30 PM', '04:00 PM']
    }
  ]);

  const [selectedProvider, setSelectedProvider] = useState<Provider>(providers[0]);

  // Bookings list
  const [bookings, setBookings] = useState<Booking[]>([
    {
      id: 'BK-1094',
      customerName: 'Alex Johnson',
      providerName: 'James Carter',
      serviceName: 'Electrical Circuit Repair',
      date: '25 Sep 2026',
      time: '05:00 PM',
      address: 'Flat 402, Oakwood Towers, Metro Blvd',
      total: 499,
      status: 'Pending'
    },
    {
      id: 'BK-1092',
      customerName: 'Emily Davis',
      providerName: 'Sarah Miller',
      serviceName: 'Bathroom Leakage Fix',
      date: '24 Sep 2026',
      time: '11:00 AM',
      address: 'House 14, Green Valley Estate',
      total: 429,
      status: 'Completed'
    }
  ]);

  // Booking Form State
  const [bookingDate, setBookingDate] = useState('25 September 2026');
  const [bookingTime, setBookingTime] = useState('05:00 PM');
  const [bookingAddress, setBookingAddress] = useState('Flat 402, Oakwood Towers, Metro Blvd');
  const [bookingProblem, setBookingProblem] = useState('Switchboard sparking & main circuit breaker tripping repeatedly.');

  // Notification toggle state
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);
  const [toastAlert, setToastAlert] = useState<{ id: string; title: string; message: string } | null>(null);

  const triggerToastAlert = (title: string, message: string) => {
    if (!notificationsEnabled) return;
    const alertId = String(Date.now());
    setToastAlert({ id: alertId, title, message });
    setTimeout(() => {
      setToastAlert(current => current?.id === alertId ? null : current);
    }, 4500);
  };

  const categories = [
    { name: 'All', icon: Wrench },
    { name: 'Repairing', icon: Zap },
    { name: 'Plumbing', icon: Droplet },
    { name: 'AC Repair', icon: AirVent },
    { name: 'Carpentry', icon: HardHat },
    { name: 'Painting', icon: Paintbrush }
  ];

  const filteredProviders = providers.filter(p => {
    const matchesCat = selectedCategory === 'All' || p.profession.toLowerCase().includes(selectedCategory.toLowerCase());
    const matchesQuery = p.name.toLowerCase().includes(searchQuery.toLowerCase()) || p.profession.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCat && matchesQuery;
  });

  const handleCreateBooking = () => {
    const newBooking: Booking = {
      id: `BK-${Math.floor(1000 + Math.random() * 9000)}`,
      customerName: 'You (Customer)',
      providerName: selectedProvider.name,
      serviceName: `${selectedProvider.profession} Service`,
      date: bookingDate,
      time: bookingTime,
      address: bookingAddress,
      total: selectedProvider.hourlyRate + 49,
      status: 'Pending'
    };
    setBookings([newBooking, ...bookings]);
    triggerToastAlert(
      '🔔 New Booking Request!',
      `Customer placed a new booking for ${selectedProvider.profession} (${bookingTime}) - ₹${selectedProvider.hourlyRate + 49}`
    );
    setSimulatorView('bookings_list');
  };

  const handleUpdateStatus = (id: string, newStatus: Booking['status']) => {
    setBookings(bookings.map(b => b.id === id ? { ...b, status: newStatus } : b));
  };

  const handleVerifyProvider = (id: string, verified: boolean) => {
    setProviders(providers.map(p => p.id === id ? { ...p, verified } : p));
  };

  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 flex flex-col font-sans">
      {/* Top Navigation Bar */}
      <header className="bg-slate-950 border-b border-slate-800 px-6 py-4 flex flex-wrap items-center justify-between gap-4 sticky top-0 z-50">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-amber-500 flex items-center justify-center text-slate-950 font-black shadow-lg shadow-amber-500/20">
            <Zap className="w-6 h-6 fill-slate-950" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="text-xl font-bold tracking-tight text-white">PlugPro</span>
              <span className="text-xs font-semibold px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-400 border border-amber-500/30">
                Android IDE Ready
              </span>
            </div>
            <p className="text-xs text-slate-400">Production-Ready Java &amp; XML Home Services Marketplace</p>
          </div>
        </div>

        {/* Mode Switcher */}
        <div className="flex bg-slate-900 p-1 rounded-xl border border-slate-800">
          <button
            onClick={() => setActiveTab('simulator')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'simulator'
                ? 'bg-amber-500 text-slate-950 shadow-md font-semibold'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <Smartphone className="w-4 h-4" />
            Mobile App Simulator
          </button>
          <button
            onClick={() => setActiveTab('admin')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'admin'
                ? 'bg-amber-500 text-slate-950 shadow-md font-semibold'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <ShieldCheck className="w-4 h-4" />
            Web Admin Panel
          </button>
          <button
            onClick={() => setActiveTab('androidide')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'androidide'
                ? 'bg-amber-500 text-slate-950 shadow-md font-semibold'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <Layers className="w-4 h-4" />
            AndroidIDE Phone Guide
          </button>
          <button
            onClick={() => setActiveTab('files')}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${
              activeTab === 'files'
                ? 'bg-amber-500 text-slate-950 shadow-md font-semibold'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <FileCode className="w-4 h-4" />
            Project File Tree
          </button>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="flex-1 p-6 max-w-7xl mx-auto w-full">
        {/* TAB 1: INTERACTIVE MOBILE SIMULATOR */}
        {activeTab === 'simulator' && (
          <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
            {/* Control Sidebar */}
            <div className="lg:col-span-4 space-y-4">
              <div className="bg-slate-950 border border-slate-800 rounded-2xl p-5 shadow-sm">
                <h3 className="text-base font-bold text-white flex items-center gap-2 mb-2">
                  <Sparkles className="w-5 h-5 text-amber-400" />
                  Interactive Android Testbed
                </h3>
                <p className="text-xs text-slate-400 mb-4 leading-relaxed">
                  Interact with the phone viewport to test customer browsing, verified professional profile, service scheduling, and provider status workflows.
                </p>

                <div className="space-y-2">
                  <div className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Quick Navigation:</div>
                  <div className="grid grid-cols-2 gap-2">
                    <button
                      onClick={() => setSimulatorView('home')}
                      className={`px-3 py-2 text-xs font-medium rounded-lg text-left transition-all ${
                        simulatorView === 'home' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/40' : 'bg-slate-900 text-slate-300 hover:bg-slate-800'
                      }`}
                    >
                      1. Customer Home
                    </button>
                    <button
                      onClick={() => setSimulatorView('provider')}
                      className={`px-3 py-2 text-xs font-medium rounded-lg text-left transition-all ${
                        simulatorView === 'provider' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/40' : 'bg-slate-900 text-slate-300 hover:bg-slate-800'
                      }`}
                    >
                      2. Pro Profile
                    </button>
                    <button
                      onClick={() => setSimulatorView('booking')}
                      className={`px-3 py-2 text-xs font-medium rounded-lg text-left transition-all ${
                        simulatorView === 'booking' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/40' : 'bg-slate-900 text-slate-300 hover:bg-slate-800'
                      }`}
                    >
                      3. Schedule Service
                    </button>
                    <button
                      onClick={() => setSimulatorView('bookings_list')}
                      className={`px-3 py-2 text-xs font-medium rounded-lg text-left transition-all ${
                        simulatorView === 'bookings_list' ? 'bg-amber-500/20 text-amber-300 border border-amber-500/40' : 'bg-slate-900 text-slate-300 hover:bg-slate-800'
                      }`}
                    >
                      4. My Bookings
                    </button>
                  </div>
                  <button
                    onClick={() => setSimulatorView('provider_dashboard')}
                    className={`w-full px-3 py-2.5 text-xs font-semibold rounded-lg text-center transition-all flex items-center justify-center gap-2 mt-2 ${
                      simulatorView === 'provider_dashboard' ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/40' : 'bg-slate-900 text-slate-300 hover:bg-slate-800'
                    }`}
                  >
                    <Briefcase className="w-4 h-4 text-emerald-400" />
                    Switch to Provider Mode
                  </button>
                </div>
              </div>

              {/* Status Stepper Helper */}
              <div className="bg-slate-950 border border-slate-800 rounded-2xl p-5 shadow-sm">
                <h4 className="text-sm font-semibold text-white mb-2">Simulate Real-Time Booking Status</h4>
                <p className="text-xs text-slate-400 mb-3">Advance the booking lifecycle in real time:</p>
                {bookings[0] ? (
                  <div className="space-y-2">
                    <div className="flex items-center justify-between text-xs bg-slate-900 p-2.5 rounded-lg border border-slate-800">
                      <span className="font-mono text-amber-400 font-bold">{bookings[0].id}</span>
                      <span className="px-2 py-0.5 rounded text-[11px] font-semibold bg-amber-500/20 text-amber-300">
                        {bookings[0].status}
                      </span>
                    </div>
                    <div className="flex flex-wrap gap-1.5 pt-1">
                      {(['Pending', 'Accepted', 'On The Way', 'Started', 'Completed', 'Cancelled'] as Booking['status'][]).map(st => (
                        <button
                          key={st}
                          onClick={() => handleUpdateStatus(bookings[0].id, st)}
                          className={`text-[11px] px-2.5 py-1 rounded font-medium transition-all ${
                            bookings[0].status === st
                              ? 'bg-amber-500 text-slate-950 font-bold'
                              : 'bg-slate-900 text-slate-400 hover:text-white border border-slate-800'
                          }`}
                        >
                          {st}
                        </button>
                      ))}
                    </div>
                  </div>
                ) : (
                  <p className="text-xs text-slate-500">No active bookings yet.</p>
                )}
              </div>
            </div>

            {/* Mobile Device Frame */}
            <div className="lg:col-span-8 flex justify-center">
              <div className="w-[380px] h-[780px] bg-slate-950 rounded-[44px] p-3 shadow-2xl border-4 border-slate-800 relative flex flex-col overflow-hidden">
                {/* Speaker Notch */}
                <div className="absolute top-4 left-1/2 -translate-x-1/2 w-28 h-4 bg-slate-900 rounded-full z-30 flex items-center justify-center">
                  <div className="w-10 h-1 bg-slate-700 rounded-full" />
                </div>

                {/* Device Screen Body */}
                <div className="flex-1 bg-slate-50 text-slate-900 rounded-[34px] overflow-hidden flex flex-col relative pt-7">
                  {/* Real-time Push Notification Toast Alert Banner */}
                  {toastAlert && (
                    <div className="absolute top-9 left-3 right-3 z-50 bg-slate-950/95 text-white p-3 rounded-2xl border border-amber-500/60 shadow-2xl backdrop-blur-md flex items-start gap-2.5 animate-in fade-in slide-in-from-top-4 duration-200">
                      <div className="w-8 h-8 rounded-xl bg-amber-500 flex items-center justify-center text-slate-950 font-black flex-shrink-0 mt-0.5">
                        <Bell className="w-4 h-4 fill-slate-950" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between">
                          <h6 className="text-[11px] font-extrabold text-amber-400">{toastAlert.title}</h6>
                          <span className="text-[9px] text-slate-400">just now</span>
                        </div>
                        <p className="text-[11px] text-slate-200 mt-0.5 leading-snug">{toastAlert.message}</p>
                      </div>
                      <button
                        onClick={() => setToastAlert(null)}
                        className="text-slate-400 hover:text-white p-0.5"
                      >
                        <X className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  )}

                  {/* SCREEN 1: CUSTOMER HOME */}
                  {simulatorView === 'home' && (
                    <div className="flex-1 flex flex-col overflow-y-auto">
                      {/* Top Header */}
                      <div className="p-4 bg-white border-b border-slate-100 flex items-center justify-between">
                        <div className="flex items-center gap-2.5">
                          <div className="w-9 h-9 rounded-xl bg-amber-500 flex items-center justify-center text-white font-black shadow-md shadow-amber-500/20">
                            <Zap className="w-5 h-5 fill-white" />
                          </div>
                          <div>
                            <div className="text-base font-extrabold text-slate-900 leading-none">PlugPro</div>
                            <div className="text-[11px] text-slate-500 mt-0.5">Find verified professionals</div>
                          </div>
                        </div>
                        <div className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center text-slate-600">
                          <Phone className="w-4 h-4" />
                        </div>
                      </div>

                      {/* Search Bar */}
                      <div className="px-4 py-3 bg-white">
                        <div className="flex items-center gap-2 bg-slate-100 px-3.5 py-2.5 rounded-2xl border border-slate-200">
                          <Search className="w-4 h-4 text-slate-400" />
                          <input
                            type="text"
                            placeholder="What service do you need?"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="bg-transparent text-xs text-slate-800 placeholder-slate-400 focus:outline-none w-full"
                          />
                        </div>
                      </div>

                      <div className="p-4 space-y-4">
                        {/* Categories Horizontal Chips */}
                        <div>
                          <div className="flex items-center justify-between mb-2.5">
                            <span className="text-xs font-bold text-slate-900">Categories</span>
                            <span className="text-[11px] font-semibold text-amber-600 cursor-pointer">See All</span>
                          </div>
                          <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
                            {categories.map((cat) => {
                              const IconComponent = cat.icon;
                              const isSelected = selectedCategory === cat.name;
                              return (
                                <button
                                  key={cat.name}
                                  onClick={() => setSelectedCategory(cat.name)}
                                  className={`flex flex-col items-center gap-1.5 p-2 rounded-2xl min-w-[62px] transition-all ${
                                    isSelected
                                      ? 'bg-amber-500 text-white shadow-md shadow-amber-500/20'
                                      : 'bg-white border border-slate-200 text-slate-700 hover:border-amber-300'
                                  }`}
                                >
                                  <div className={`w-8 h-8 rounded-xl flex items-center justify-center ${isSelected ? 'bg-white/20' : 'bg-amber-50 text-amber-600'}`}>
                                    <IconComponent className="w-4 h-4" />
                                  </div>
                                  <span className="text-[10px] font-medium truncate max-w-[56px]">{cat.name}</span>
                                </button>
                              );
                            })}
                          </div>
                        </div>

                        {/* Promotional Banner */}
                        <div className="bg-gradient-to-r from-amber-500 to-amber-600 rounded-3xl p-4 text-white shadow-lg shadow-amber-500/15 flex items-center justify-between">
                          <div className="space-y-2">
                            <h4 className="text-xs font-bold leading-tight">Need help with repairs or installation?</h4>
                            <button
                              onClick={() => setSimulatorView('booking')}
                              className="px-3 py-1.5 bg-white text-amber-700 rounded-xl text-[11px] font-bold shadow-sm"
                            >
                              Book Now
                            </button>
                          </div>
                          <div className="w-14 h-14 rounded-2xl bg-white/20 flex items-center justify-center">
                            <Wrench className="w-7 h-7 text-white" />
                          </div>
                        </div>

                        {/* Recommended Professionals List */}
                        <div>
                          <div className="flex items-center justify-between mb-3">
                            <span className="text-xs font-bold text-slate-900">Recommended Professionals</span>
                            <span className="text-[11px] text-slate-500">{filteredProviders.length} available</span>
                          </div>

                          <div className="space-y-3">
                            {filteredProviders.map(pro => (
                              <div
                                key={pro.id}
                                onClick={() => {
                                  setSelectedProvider(pro);
                                  setSimulatorView('provider');
                                }}
                                className="bg-white p-3.5 rounded-2xl border border-slate-200 shadow-sm hover:border-amber-400 transition-all cursor-pointer"
                              >
                                <div className="flex items-center gap-3">
                                  <div className="w-12 h-12 rounded-full bg-slate-200 border-2 border-amber-400 overflow-hidden flex items-center justify-center text-slate-600 font-bold text-sm">
                                    {pro.name.split(' ').map(n => n[0]).join('')}
                                  </div>
                                  <div className="flex-1 min-w-0">
                                    <div className="flex items-center gap-1.5">
                                      <h5 className="text-xs font-bold text-slate-900 truncate">{pro.name}</h5>
                                      {pro.verified && (
                                        <CheckCircle className="w-3.5 h-3.5 text-emerald-500 fill-emerald-100" />
                                      )}
                                    </div>
                                    <p className="text-[11px] text-slate-500 truncate">{pro.profession}</p>
                                    <div className="flex items-center gap-2 mt-1">
                                      <div className="flex items-center gap-1 text-[11px] font-bold text-slate-900">
                                        <Star className="w-3 h-3 fill-amber-400 text-amber-400" />
                                        {pro.rating}
                                      </div>
                                      <span className="text-[10px] text-slate-400">({pro.reviews})</span>
                                      <span className="text-[10px] text-slate-300">•</span>
                                      <span className="text-[10px] font-medium text-slate-600">{pro.experience} yrs exp</span>
                                    </div>
                                  </div>
                                  <div className="text-right">
                                    <div className="text-xs font-extrabold text-amber-600">₹{pro.hourlyRate}<span className="text-[10px] font-normal text-slate-400">/hr</span></div>
                                    <button
                                      onClick={(e) => {
                                        e.stopPropagation();
                                        setSelectedProvider(pro);
                                        setSimulatorView('booking');
                                      }}
                                      className="mt-1.5 px-2.5 py-1 bg-amber-500 hover:bg-amber-600 text-white rounded-lg text-[10px] font-bold"
                                    >
                                      Book
                                    </button>
                                  </div>
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>
                      </div>
                    </div>
                  )}

                  {/* SCREEN 2: PROFESSIONAL DETAIL */}
                  {simulatorView === 'provider' && (
                    <div className="flex-1 flex flex-col overflow-y-auto bg-slate-50">
                      {/* Top Nav */}
                      <div className="p-4 bg-white border-b border-slate-100 flex items-center justify-between sticky top-0 z-10">
                        <button onClick={() => setSimulatorView('home')} className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center">
                          <ArrowLeft className="w-4 h-4 text-slate-700" />
                        </button>
                        <span className="text-xs font-bold text-slate-900">Professional Profile</span>
                        <div className="w-8 h-8" />
                      </div>

                      <div className="p-4 space-y-4">
                        {/* Profile Header */}
                        <div className="bg-white p-4 rounded-3xl border border-slate-200 text-center space-y-2">
                          <div className="w-16 h-16 rounded-full bg-amber-100 border-2 border-amber-500 mx-auto flex items-center justify-center text-amber-700 font-black text-lg">
                            {selectedProvider.name.split(' ').map(n => n[0]).join('')}
                          </div>
                          <div>
                            <div className="flex items-center justify-center gap-1.5">
                              <h4 className="text-sm font-extrabold text-slate-900">{selectedProvider.name}</h4>
                              {selectedProvider.verified && (
                                <CheckCircle className="w-4 h-4 text-emerald-500 fill-emerald-100" />
                              )}
                            </div>
                            <p className="text-xs text-slate-500">{selectedProvider.profession}</p>
                            <p className="text-[11px] text-slate-400 flex items-center justify-center gap-1 mt-0.5">
                              <MapPin className="w-3 h-3 text-red-500" /> {selectedProvider.serviceArea}
                            </p>
                          </div>

                          {/* Stats Grid */}
                          <div className="grid grid-cols-3 gap-2 pt-3 border-t border-slate-100">
                            <div className="bg-slate-50 p-2 rounded-xl">
                              <div className="text-xs font-bold text-amber-600">{selectedProvider.rating} ★</div>
                              <div className="text-[10px] text-slate-400">Rating</div>
                            </div>
                            <div className="bg-slate-50 p-2 rounded-xl">
                              <div className="text-xs font-bold text-slate-800">{selectedProvider.experience} Yrs</div>
                              <div className="text-[10px] text-slate-400">Experience</div>
                            </div>
                            <div className="bg-slate-50 p-2 rounded-xl">
                              <div className="text-xs font-bold text-slate-800">{selectedProvider.reviews}+</div>
                              <div className="text-[10px] text-slate-400">Jobs Done</div>
                            </div>
                          </div>
                        </div>

                        {/* About Section */}
                        <div className="bg-white p-4 rounded-2xl border border-slate-200 space-y-1.5">
                          <h5 className="text-xs font-bold text-slate-900">About Professional</h5>
                          <p className="text-[11px] text-slate-600 leading-relaxed">{selectedProvider.about}</p>
                        </div>

                        {/* Availability Section */}
                        <div className="bg-white p-4 rounded-2xl border border-slate-200 space-y-2">
                          <h5 className="text-xs font-bold text-slate-900">Available Time Slots</h5>
                          <div className="flex flex-wrap gap-1.5">
                            {selectedProvider.availableSlots.map(slot => (
                              <span key={slot} className="px-2.5 py-1 bg-amber-50 text-amber-700 text-[10px] font-semibold rounded-lg border border-amber-200">
                                {slot}
                              </span>
                            ))}
                          </div>
                        </div>
                      </div>

                      {/* Bottom Sticky Action Bar */}
                      <div className="p-3 bg-white border-t border-slate-200 flex items-center gap-2 mt-auto">
                        <button
                          onClick={() => alert(`Calling ${selectedProvider.name} at ${selectedProvider.phone}`)}
                          className="w-10 h-10 rounded-xl bg-slate-100 flex items-center justify-center text-slate-700"
                        >
                          <Phone className="w-4 h-4 text-emerald-600" />
                        </button>
                        <button
                          onClick={() => alert(`Opening encrypted Firestore chat with ${selectedProvider.name}`)}
                          className="w-10 h-10 rounded-xl bg-slate-100 flex items-center justify-center text-slate-700"
                        >
                          <MessageSquare className="w-4 h-4 text-amber-600" />
                        </button>
                        <button
                          onClick={() => setSimulatorView('booking')}
                          className="flex-1 py-2.5 bg-amber-500 hover:bg-amber-600 text-white rounded-xl text-xs font-bold shadow-md shadow-amber-500/20"
                        >
                          Schedule Service • ₹{selectedProvider.hourlyRate}/hr
                        </button>
                      </div>
                    </div>
                  )}

                  {/* SCREEN 3: BOOKING SCHEDULER */}
                  {simulatorView === 'booking' && (
                    <div className="flex-1 flex flex-col overflow-y-auto bg-slate-50">
                      <div className="p-4 bg-white border-b border-slate-100 flex items-center justify-between sticky top-0 z-10">
                        <button onClick={() => setSimulatorView('home')} className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center">
                          <ArrowLeft className="w-4 h-4 text-slate-700" />
                        </button>
                        <span className="text-xs font-bold text-slate-900">Schedule Service</span>
                        <div className="w-8 h-8" />
                      </div>

                      <div className="p-4 space-y-4">
                        {/* Service & Provider */}
                        <div className="bg-white p-3.5 rounded-2xl border border-slate-200 flex items-center justify-between">
                          <div>
                            <span className="text-[10px] text-slate-400 uppercase font-semibold">Service With</span>
                            <div className="text-xs font-bold text-slate-900">{selectedProvider.name}</div>
                            <div className="text-[11px] text-slate-500">{selectedProvider.profession}</div>
                          </div>
                          <div className="text-right">
                            <span className="text-[10px] text-slate-400">Rate</span>
                            <div className="text-xs font-bold text-amber-600">₹{selectedProvider.hourlyRate}/hr</div>
                          </div>
                        </div>

                        {/* Date & Time Selection */}
                        <div className="bg-white p-3.5 rounded-2xl border border-slate-200 space-y-3">
                          <div>
                            <label className="text-[11px] font-semibold text-slate-700 flex items-center gap-1.5 mb-1">
                              <Calendar className="w-3.5 h-3.5 text-amber-500" /> Date
                            </label>
                            <input
                              type="text"
                              value={bookingDate}
                              onChange={(e) => setBookingDate(e.target.value)}
                              className="w-full text-xs p-2 rounded-xl border border-slate-200 bg-slate-50 font-medium"
                            />
                          </div>

                          <div>
                            <label className="text-[11px] font-semibold text-slate-700 flex items-center gap-1.5 mb-1">
                              <Clock className="w-3.5 h-3.5 text-amber-500" /> Preferred Time
                            </label>
                            <div className="grid grid-cols-2 gap-2">
                              {selectedProvider.availableSlots.slice(0, 4).map(slot => (
                                <button
                                  key={slot}
                                  onClick={() => setBookingTime(slot)}
                                  className={`text-[11px] p-2 rounded-xl font-medium border text-center transition-all ${
                                    bookingTime === slot ? 'bg-amber-500 text-white border-amber-500 font-bold' : 'bg-slate-50 text-slate-700 border-slate-200'
                                  }`}
                                >
                                  {slot}
                                </button>
                              ))}
                            </div>
                          </div>
                        </div>

                        {/* Address */}
                        <div className="bg-white p-3.5 rounded-2xl border border-slate-200 space-y-1.5">
                          <label className="text-[11px] font-semibold text-slate-700 flex items-center gap-1.5">
                            <MapPin className="w-3.5 h-3.5 text-red-500" /> Service Address
                          </label>
                          <textarea
                            rows={2}
                            value={bookingAddress}
                            onChange={(e) => setBookingAddress(e.target.value)}
                            className="w-full text-xs p-2 rounded-xl border border-slate-200 bg-slate-50"
                          />
                        </div>

                        {/* Problem Description */}
                        <div className="bg-white p-3.5 rounded-2xl border border-slate-200 space-y-1.5">
                          <label className="text-[11px] font-semibold text-slate-700">Problem Description</label>
                          <textarea
                            rows={2}
                            value={bookingProblem}
                            onChange={(e) => setBookingProblem(e.target.value)}
                            className="w-full text-xs p-2 rounded-xl border border-slate-200 bg-slate-50"
                          />
                        </div>

                        {/* Price Breakdown */}
                        <div className="bg-white p-3.5 rounded-2xl border border-slate-200 space-y-2">
                          <div className="flex justify-between text-xs text-slate-600">
                            <span>Service Estimate</span>
                            <span className="font-semibold text-slate-900">₹{selectedProvider.hourlyRate}</span>
                          </div>
                          <div className="flex justify-between text-xs text-slate-600">
                            <span>Convenience Fee</span>
                            <span className="font-semibold text-slate-900">₹49</span>
                          </div>
                          <div className="border-t border-slate-100 pt-2 flex justify-between text-xs font-bold text-slate-900">
                            <span>Total (Pay on service)</span>
                            <span className="text-amber-600 font-extrabold">₹{selectedProvider.hourlyRate + 49}</span>
                          </div>
                        </div>
                      </div>

                      {/* Confirm CTA */}
                      <div className="p-3 bg-white border-t border-slate-200 mt-auto">
                        <button
                          onClick={handleCreateBooking}
                          className="w-full py-2.5 bg-amber-500 hover:bg-amber-600 text-white rounded-xl text-xs font-bold shadow-md shadow-amber-500/20"
                        >
                          Confirm &amp; Place Booking (₹{selectedProvider.hourlyRate + 49})
                        </button>
                      </div>
                    </div>
                  )}

                  {/* SCREEN 4: MY BOOKINGS LIST */}
                  {simulatorView === 'bookings_list' && (
                    <div className="flex-1 flex flex-col overflow-y-auto bg-slate-50">
                      <div className="p-4 bg-white border-b border-slate-100 flex items-center justify-between sticky top-0 z-10">
                        <button onClick={() => setSimulatorView('home')} className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center">
                          <ArrowLeft className="w-4 h-4 text-slate-700" />
                        </button>
                        <span className="text-xs font-bold text-slate-900">My Bookings</span>
                        <div className="w-8 h-8" />
                      </div>

                      <div className="p-4 space-y-3">
                        {bookings.map(b => (
                          <div key={b.id} className="bg-white p-3.5 rounded-2xl border border-slate-200 space-y-2.5 shadow-sm">
                            <div className="flex items-center justify-between">
                              <span className="text-[10px] font-mono font-bold text-slate-400">{b.id}</span>
                              <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                                b.status === 'Completed' ? 'bg-emerald-100 text-emerald-700' :
                                b.status === 'Pending' ? 'bg-amber-100 text-amber-700' :
                                b.status === 'Cancelled' ? 'bg-red-100 text-red-700' :
                                'bg-blue-100 text-blue-700'
                              }`}>
                                {b.status}
                              </span>
                            </div>

                            <div>
                              <h5 className="text-xs font-bold text-slate-900">{b.serviceName}</h5>
                              <p className="text-[11px] text-slate-500">Professional: {b.providerName}</p>
                            </div>

                            <div className="flex items-center gap-3 text-[11px] text-slate-600 bg-slate-50 p-2 rounded-xl">
                              <div className="flex items-center gap-1">
                                <Calendar className="w-3 h-3 text-amber-500" /> {b.date}
                              </div>
                              <div className="flex items-center gap-1">
                                <Clock className="w-3 h-3 text-amber-500" /> {b.time}
                              </div>
                            </div>

                            <div className="flex items-center justify-between pt-1 border-t border-slate-100">
                              <span className="text-[11px] text-slate-400">Total: <strong className="text-slate-900">₹{b.total}</strong></span>
                              {b.status === 'Pending' && (
                                <button
                                  onClick={() => handleUpdateStatus(b.id, 'Cancelled')}
                                  className="text-[10px] text-red-600 font-semibold hover:underline"
                                >
                                  Cancel Booking
                                </button>
                              )}
                              {b.status === 'Completed' && (
                                <button
                                  onClick={() => alert('Review submitted! Professional rating updated.')}
                                  className="text-[10px] text-amber-600 font-semibold hover:underline"
                                >
                                  Rate &amp; Review ★
                                </button>
                              )}
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* SCREEN 5: PROVIDER DASHBOARD */}
                  {simulatorView === 'provider_dashboard' && (
                    <div className="flex-1 flex flex-col overflow-y-auto bg-slate-50">
                      <div className="p-4 bg-slate-900 text-white flex items-center justify-between sticky top-0 z-10">
                        <div>
                          <span className="text-[10px] text-amber-400 font-bold uppercase tracking-wider">Provider Mode</span>
                          <h4 className="text-xs font-bold">Partner Dashboard</h4>
                        </div>
                        <button
                          onClick={() => setSimulatorView('home')}
                          className="px-2.5 py-1 bg-slate-800 text-[10px] rounded-lg text-slate-200"
                        >
                          Exit Mode
                        </button>
                      </div>

                      <div className="p-4 space-y-4">
                        {/* Status Card */}
                        <div className="bg-emerald-50 border border-emerald-200 p-3 rounded-2xl flex items-center gap-3">
                          <CheckCircle className="w-6 h-6 text-emerald-600 flex-shrink-0" />
                          <div>
                            <div className="text-xs font-bold text-emerald-900">Verified Professional</div>
                            <p className="text-[10px] text-emerald-700">Your profile is active in marketplace search.</p>
                          </div>
                        </div>

                        {/* Push Notification Toggle Switch Card */}
                        <div className="bg-white p-3.5 rounded-2xl border border-slate-200 shadow-sm space-y-2.5">
                          <div className="flex items-center justify-between">
                            <div className="flex items-center gap-2.5">
                              <div className={`w-8 h-8 rounded-xl flex items-center justify-center transition-colors ${notificationsEnabled ? 'bg-amber-100 text-amber-600' : 'bg-slate-100 text-slate-400'}`}>
                                <Bell className="w-4 h-4" />
                              </div>
                              <div>
                                <div className="text-xs font-bold text-slate-900">Toggle Notifications</div>
                                <div className="text-[10px] text-slate-500">Live toast alert on incoming bookings</div>
                              </div>
                            </div>

                            {/* Custom Toggle Switch */}
                            <button
                              onClick={() => {
                                const nextState = !notificationsEnabled;
                                setNotificationsEnabled(nextState);
                                if (nextState) {
                                  triggerToastAlert('🔔 Notifications Enabled', 'You will receive live toast alerts for new bookings');
                                }
                              }}
                              className={`w-11 h-6 rounded-full transition-colors relative p-0.5 focus:outline-none ${
                                notificationsEnabled ? 'bg-amber-500' : 'bg-slate-300'
                              }`}
                            >
                              <div
                                className={`w-5 h-5 rounded-full bg-white shadow-md transform transition-transform ${
                                  notificationsEnabled ? 'translate-x-5' : 'translate-x-0'
                                }`}
                              />
                            </button>
                          </div>

                          <button
                            onClick={() => {
                              if (notificationsEnabled) {
                                triggerToastAlert(
                                  '🔔 New Booking Request!',
                                  'Customer Alex Johnson requested Electrical Circuit Repair for 05:00 PM (₹499)'
                                );
                              } else {
                                alert('Notifications are currently toggled OFF. Turn ON the switch above to receive live toast alerts.');
                              }
                            }}
                            className="w-full py-2 bg-amber-50 hover:bg-amber-100 text-amber-700 border border-amber-200 rounded-xl text-[11px] font-bold transition-all flex items-center justify-center gap-1.5"
                          >
                            <Zap className="w-3.5 h-3.5" />
                            Test Push Notification Toast
                          </button>
                        </div>

                        {/* Earnings Metrics */}
                        <div className="grid grid-cols-2 gap-2">
                          <div className="bg-white p-3 rounded-2xl border border-slate-200">
                            <span className="text-[10px] text-slate-400">Total Earnings</span>
                            <div className="text-sm font-black text-amber-600">₹14,850</div>
                          </div>
                          <div className="bg-white p-3 rounded-2xl border border-slate-200">
                            <span className="text-[10px] text-slate-400">Active Jobs</span>
                            <div className="text-sm font-black text-slate-800">
                              {bookings.filter(b => b.status === 'Accepted' || b.status === 'On The Way' || b.status === 'Started').length}
                            </div>
                          </div>
                        </div>

                        {/* Client Booking Requests */}
                        <div>
                          <div className="text-xs font-bold text-slate-900 mb-2">Pending Client Requests</div>
                          <div className="space-y-2.5">
                            {bookings.filter(b => b.status === 'Pending').map(b => (
                              <div key={b.id} className="bg-white p-3 rounded-2xl border border-slate-200 space-y-2">
                                <div className="flex justify-between items-center text-xs">
                                  <span className="font-bold text-slate-900">{b.customerName}</span>
                                  <span className="text-amber-600 font-bold">₹{b.total}</span>
                                </div>
                                <p className="text-[11px] text-slate-500">{b.serviceName} • {b.date} at {b.time}</p>
                                <p className="text-[10px] text-slate-400 truncate">{b.address}</p>
                                <div className="flex gap-2 pt-1">
                                  <button
                                    onClick={() => handleUpdateStatus(b.id, 'Accepted')}
                                    className="flex-1 py-1.5 bg-emerald-600 text-white rounded-lg text-[10px] font-bold"
                                  >
                                    Accept Request
                                  </button>
                                  <button
                                    onClick={() => handleUpdateStatus(b.id, 'Cancelled')}
                                    className="px-3 py-1.5 bg-slate-100 text-red-600 rounded-lg text-[10px] font-bold"
                                  >
                                    Decline
                                  </button>
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Android Bottom Navigation */}
                  <div className="bg-white border-t border-slate-200 py-2 px-6 flex items-center justify-between text-slate-400">
                    <button
                      onClick={() => setSimulatorView('home')}
                      className={`flex flex-col items-center gap-0.5 ${simulatorView === 'home' ? 'text-amber-500 font-bold' : ''}`}
                    >
                      <Zap className="w-4 h-4" />
                      <span className="text-[9px]">Home</span>
                    </button>
                    <button
                      onClick={() => setSimulatorView('home')}
                      className="flex flex-col items-center gap-0.5"
                    >
                      <Wrench className="w-4 h-4" />
                      <span className="text-[9px]">Services</span>
                    </button>
                    <button
                      onClick={() => setSimulatorView('bookings_list')}
                      className={`flex flex-col items-center gap-0.5 ${simulatorView === 'bookings_list' ? 'text-amber-500 font-bold' : ''}`}
                    >
                      <Calendar className="w-4 h-4" />
                      <span className="text-[9px]">Bookings</span>
                    </button>
                    <button
                      onClick={() => setSimulatorView('provider_dashboard')}
                      className={`flex flex-col items-center gap-0.5 ${simulatorView === 'provider_dashboard' ? 'text-amber-500 font-bold' : ''}`}
                    >
                      <Briefcase className="w-4 h-4" />
                      <span className="text-[9px]">Provider</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* TAB 2: WEB ADMIN PANEL */}
        {activeTab === 'admin' && (
          <div className="space-y-6">
            {/* Admin Header Stats */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="bg-slate-950 p-5 rounded-2xl border border-slate-800">
                <span className="text-xs text-slate-400 font-medium">Total Platform Volume</span>
                <div className="text-2xl font-black text-amber-400 mt-1">₹184,500</div>
                <span className="text-[11px] text-emerald-400 flex items-center gap-1 mt-1">
                  <TrendingUp className="w-3 h-3" /> +18.4% this month
                </span>
              </div>
              <div className="bg-slate-950 p-5 rounded-2xl border border-slate-800">
                <span className="text-xs text-slate-400 font-medium">Active Bookings</span>
                <div className="text-2xl font-black text-white mt-1">{bookings.length}</div>
                <span className="text-[11px] text-slate-400 mt-1 block">Live across all regions</span>
              </div>
              <div className="bg-slate-950 p-5 rounded-2xl border border-slate-800">
                <span className="text-xs text-slate-400 font-medium">Verified Professionals</span>
                <div className="text-2xl font-black text-emerald-400 mt-1">
                  {providers.filter(p => p.verified).length}
                </div>
                <span className="text-[11px] text-slate-400 mt-1 block">Background check approved</span>
              </div>
              <div className="bg-slate-950 p-5 rounded-2xl border border-slate-800">
                <span className="text-xs text-slate-400 font-medium">Pending Verifications</span>
                <div className="text-2xl font-black text-amber-500 mt-1">
                  {providers.filter(p => !p.verified).length}
                </div>
                <span className="text-[11px] text-amber-400/80 mt-1 block">Requires manual review</span>
              </div>
            </div>

            {/* Provider Verification Table */}
            <div className="bg-slate-950 rounded-2xl border border-slate-800 overflow-hidden">
              <div className="p-5 border-b border-slate-800 flex justify-between items-center">
                <div>
                  <h3 className="text-base font-bold text-white">Technician Verification &amp; Activation</h3>
                  <p className="text-xs text-slate-400">Review technician credentials and grant marketplace verified status.</p>
                </div>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs">
                  <thead className="bg-slate-900/60 text-slate-400 uppercase tracking-wider font-semibold border-b border-slate-800">
                    <tr>
                      <th className="p-4">Technician</th>
                      <th className="p-4">Profession</th>
                      <th className="p-4">Experience</th>
                      <th className="p-4">Hourly Fee</th>
                      <th className="p-4">Status</th>
                      <th className="p-4">Admin Action</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800 text-slate-300">
                    {providers.map(pro => (
                      <tr key={pro.id} className="hover:bg-slate-900/30">
                        <td className="p-4 font-bold text-white flex items-center gap-2">
                          <div className="w-8 h-8 rounded-full bg-slate-800 flex items-center justify-center text-xs text-amber-400">
                            {pro.name[0]}
                          </div>
                          {pro.name}
                        </td>
                        <td className="p-4">{pro.profession}</td>
                        <td className="p-4">{pro.experience} Years</td>
                        <td className="p-4 font-mono font-bold text-amber-400">₹{pro.hourlyRate}/hr</td>
                        <td className="p-4">
                          {pro.verified ? (
                            <span className="px-2.5 py-1 rounded-full text-[11px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                              Verified
                            </span>
                          ) : (
                            <span className="px-2.5 py-1 rounded-full text-[11px] font-semibold bg-amber-500/10 text-amber-400 border border-amber-500/20">
                              Pending Approval
                            </span>
                          )}
                        </td>
                        <td className="p-4">
                          {pro.verified ? (
                            <button
                              onClick={() => handleVerifyProvider(pro.id, false)}
                              className="px-3 py-1 bg-red-500/10 hover:bg-red-500/20 text-red-400 border border-red-500/30 rounded-lg text-[11px] font-medium transition-all"
                            >
                              Revoke Badge
                            </button>
                          ) : (
                            <button
                              onClick={() => handleVerifyProvider(pro.id, true)}
                              className="px-3 py-1 bg-emerald-500 hover:bg-emerald-600 text-slate-950 font-bold rounded-lg text-[11px] shadow-sm transition-all"
                            >
                              Approve &amp; Verify
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Live Bookings Table */}
            <div className="bg-slate-950 rounded-2xl border border-slate-800 overflow-hidden">
              <div className="p-5 border-b border-slate-800">
                <h3 className="text-base font-bold text-white">Live Customer Bookings Management</h3>
                <p className="text-xs text-slate-400">Supervise order statuses, fees, and client addresses.</p>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs">
                  <thead className="bg-slate-900/60 text-slate-400 uppercase tracking-wider font-semibold border-b border-slate-800">
                    <tr>
                      <th className="p-4">ID</th>
                      <th className="p-4">Customer</th>
                      <th className="p-4">Service</th>
                      <th className="p-4">Assigned Pro</th>
                      <th className="p-4">Scheduled Slot</th>
                      <th className="p-4">Total Fee</th>
                      <th className="p-4">Status</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800 text-slate-300">
                    {bookings.map(b => (
                      <tr key={b.id} className="hover:bg-slate-900/30">
                        <td className="p-4 font-mono font-bold text-amber-400">{b.id}</td>
                        <td className="p-4 font-semibold text-white">{b.customerName}</td>
                        <td className="p-4">{b.serviceName}</td>
                        <td className="p-4">{b.providerName}</td>
                        <td className="p-4">{b.date}, {b.time}</td>
                        <td className="p-4 font-mono font-bold text-slate-100">₹{b.total}</td>
                        <td className="p-4">
                          <span className={`px-2.5 py-1 rounded-full text-[11px] font-semibold ${
                            b.status === 'Completed' ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' :
                            b.status === 'Pending' ? 'bg-amber-500/10 text-amber-400 border border-amber-500/20' :
                            b.status === 'Cancelled' ? 'bg-red-500/10 text-red-400 border border-red-500/20' :
                            'bg-blue-500/10 text-blue-400 border border-blue-500/20'
                          }`}>
                            {b.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        )}

        {/* TAB 3: ANDROIDIDE PHONE BUILD GUIDE */}
        {activeTab === 'androidide' && (
          <div className="bg-slate-950 p-6 rounded-3xl border border-slate-800 space-y-6">
            <div>
              <h3 className="text-xl font-bold text-white flex items-center gap-2">
                <Smartphone className="w-6 h-6 text-amber-500" />
                How to Build &amp; Run PlugPro on AndroidIDE (Phone Only)
              </h3>
              <p className="text-xs text-slate-400 mt-1">
                This project has been architected strictly in Java and XML layouts with clean Gradle version 8 compatibility for effortless phone compilation.
              </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="bg-slate-900 p-5 rounded-2xl border border-slate-800 space-y-3">
                <h4 className="text-sm font-bold text-amber-400 flex items-center gap-2">
                  <span className="w-5 h-5 rounded-full bg-amber-500/20 text-amber-400 flex items-center justify-center text-xs">1</span>
                  Install AndroidIDE on Your Device
                </h4>
                <p className="text-xs text-slate-300 leading-relaxed">
                  Download AndroidIDE from F-Droid or GitHub releases. Open AndroidIDE and let it download JDK 17 and the Android SDK tools (API 34).
                </p>
                <div className="bg-slate-950 p-3 rounded-xl border border-slate-800 font-mono text-[11px] text-slate-300">
                  pkg install openjdk-17<br />
                  sdkmanager "platforms;android-34" "build-tools;34.0.0"
                </div>
              </div>

              <div className="bg-slate-900 p-5 rounded-2xl border border-slate-800 space-y-3">
                <h4 className="text-sm font-bold text-amber-400 flex items-center gap-2">
                  <span className="w-5 h-5 rounded-full bg-amber-500/20 text-amber-400 flex items-center justify-center text-xs">2</span>
                  Open the Project
                </h4>
                <p className="text-xs text-slate-300 leading-relaxed">
                  Clone or copy this repository to your device's internal storage (e.g., <code className="text-amber-400">/storage/emulated/0/AndroidIDEProjects/PlugPro</code>). Tap "Open Project" in AndroidIDE.
                </p>
                <div className="bg-slate-950 p-3 rounded-xl border border-slate-800 font-mono text-[11px] text-slate-300">
                  git clone https://github.com/plugpro/android-app.git PlugPro<br />
                  cd PlugPro
                </div>
              </div>

              <div className="bg-slate-900 p-5 rounded-2xl border border-slate-800 space-y-3">
                <h4 className="text-sm font-bold text-amber-400 flex items-center gap-2">
                  <span className="w-5 h-5 rounded-full bg-amber-500/20 text-amber-400 flex items-center justify-center text-xs">3</span>
                  Connect Firebase Configuration
                </h4>
                <p className="text-xs text-slate-300 leading-relaxed">
                  The project includes a ready-to-use <code className="text-amber-400">app/google-services.json</code>. You can paste your own project's <code className="text-amber-400">google-services.json</code> from Firebase Console.
                </p>
                <div className="bg-slate-950 p-3 rounded-xl border border-slate-800 text-[11px] text-slate-300">
                  Enable <strong>Email/Password Auth</strong>, <strong>Cloud Firestore</strong>, and <strong>Firebase Storage</strong> in your Firebase Console.
                </div>
              </div>

              <div className="bg-slate-900 p-5 rounded-2xl border border-slate-800 space-y-3">
                <h4 className="text-sm font-bold text-amber-400 flex items-center gap-2">
                  <span className="w-5 h-5 rounded-full bg-amber-500/20 text-amber-400 flex items-center justify-center text-xs">4</span>
                  Build APK &amp; Run
                </h4>
                <p className="text-xs text-slate-300 leading-relaxed">
                  In AndroidIDE, tap the green <strong>Run / Build</strong> icon or execute the Gradle wrapper directly in the terminal:
                </p>
                <div className="bg-slate-950 p-3 rounded-xl border border-slate-800 font-mono text-[11px] text-emerald-400">
                  ./gradlew assembleDebug
                </div>
                <p className="text-[11px] text-slate-400">
                  Output APK: <code className="text-slate-300">app/build/outputs/apk/debug/app-debug.apk</code>
                </p>
              </div>
            </div>
          </div>
        )}

        {/* TAB 4: PROJECT FILE TREE */}
        {activeTab === 'files' && (
          <div className="bg-slate-950 p-6 rounded-3xl border border-slate-800 space-y-4">
            <h3 className="text-lg font-bold text-white flex items-center gap-2">
              <FileCode className="w-5 h-5 text-amber-500" />
              Generated Android Project Architecture
            </h3>
            <p className="text-xs text-slate-400">
              Clean MVVM package layout in Java with strict separation between Models, Repositories, ViewBinding UI controllers, and Firebase services.
            </p>

            <div className="bg-slate-900 p-4 rounded-2xl border border-slate-800 font-mono text-xs text-slate-300 space-y-1 overflow-x-auto">
              <div>📁 <strong>app/</strong></div>
              <div className="pl-4">├── 📄 build.gradle (AGP 8.2.2, Material3, Firebase BOM, Glide)</div>
              <div className="pl-4">├── 📄 google-services.json</div>
              <div className="pl-4">└── 📁 src/main/</div>
              <div className="pl-8">├── 📄 AndroidManifest.xml (Permissions &amp; Activities registered)</div>
              <div className="pl-8">├── 📁 java/com/plugpro/</div>
              <div className="pl-12">├── 📄 PlugProApplication.java (FCM &amp; Firestore auto-seed)</div>
              <div className="pl-12">├── 📁 data/model/ (User, ServiceProvider, Booking, Review, Chat)</div>
              <div className="pl-12">├── 📁 data/repository/ (AuthRepository, ProviderRepository, BookingRepository, ChatRepository)</div>
              <div className="pl-12">├── 📁 ui/auth/ (Splash, Onboarding, Login, Signup, ForgotPassword)</div>
              <div className="pl-12">├── 📁 ui/customer/ (MainActivity, Home, Services, Bookings, Chat, Profile)</div>
              <div className="pl-12">├── 📁 ui/provider/ (ProviderMain, Dashboard, Bookings, Availability, Registration)</div>
              <div className="pl-12">├── 📁 ui/booking/ (BookingScheduleActivity, BookingDetailActivity)</div>
              <div className="pl-12">├── 📁 ui/providers/ (ProviderDetailActivity)</div>
              <div className="pl-12">├── 📁 ui/adapters/ (Category, Provider, Booking, Chat, Message, Review)</div>
              <div className="pl-12">├── 📁 notification/ (PlugProMessagingService)</div>
              <div className="pl-12">└── 📁 utils/ (FirebaseUtil, PreferenceHelper, DateTimeUtil, SeedDataUtil)</div>
              <div className="pl-8">└── 📁 res/</div>
              <div className="pl-12">├── 📁 layout/ (24 pristine XML responsive layout files)</div>
              <div className="pl-12">├── 📁 values/ (colors, dimens, strings, Material3 themes)</div>
              <div className="pl-12">├── 📁 values-night/ (Dark mode themes)</div>
              <div className="pl-12">├── 📁 drawable/ (Vector drawables &amp; cards)</div>
              <div className="pl-12">└── 📁 menu/ (Customer &amp; Provider bottom navigation menus)</div>
              <div>📁 <strong>firebase/</strong> (firestore.rules, firestore.indexes.json, storage.rules)</div>
              <div>📁 <strong>admin/</strong> (PHP MySQL Web Administrator Portal)</div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
